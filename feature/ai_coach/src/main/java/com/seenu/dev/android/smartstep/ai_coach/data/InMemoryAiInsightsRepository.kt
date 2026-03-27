package com.seenu.dev.android.smartstep.ai_coach.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

class InMemoryAiInsightsRepository(
    private val geminiRepository: GeminiRepository,
    private val config: AiInsightsConfig = AiInsightsConfig(),
    private val appScope: CoroutineScope? = null // optional shared scope for de-dupe tasks
) : AiInsightsRepository {

    private data class CacheKey(
        val dateKey: String, // yyyy-MM-dd
        val timeBucket: String, // morning/afternoon/evening/night
        val stepGoal: Int,
        val goalPctBucket: Int // 0,25,50,75,100
    )

    private data class Entry(
        val insight: String,
        val generatedAtMillis: Long,
        val stepsAt: Int,
        val goalPctAt: Int
    )

    private val cache = ConcurrentHashMap<CacheKey, Entry>()
    private val inFlight = HashMap<CacheKey, Deferred<String>>()
    private val mutex = Mutex()
    private val lock = Any()
    private companion object {
        private const val FALLBACK_INSIGHT = "Keep moving — every step counts!"
    }

    override fun forceRefresh() {
        cache.clear()
    }

    override suspend fun getInsight(
        currentSteps: Int,
        stepGoal: Int,
        timeOfDay: String,
        nowMillis: Long
    ): String {
        val goalPct = if (stepGoal > 0) (currentSteps * 100 / stepGoal).coerceIn(0, 200) else 0
        val key = CacheKey(
            dateKey = toDateKey(nowMillis),
            timeBucket = timeBucketOf(timeOfDay),
            stepGoal = stepGoal,
            goalPctBucket = bucket(goalPct)
        )

        val cached = cache[key]
        if (cached != null) {
            val ageMin = (nowMillis - cached.generatedAtMillis) / 60000L
            val stepDelta = kotlin.math.abs(currentSteps - cached.stepsAt)
            val freshByTtl = ageMin < config.ttlMinutes
            val freshByDelta = stepDelta < config.stepDeltaThreshold
            if (freshByTtl && freshByDelta) return cached.insight
        }

        // De-duplicate concurrent calls per key
        return mutex.withLock {
            inFlight[key]?.let { return@withLock it }
            val scope = appScope ?: CoroutineScope(Dispatchers.IO)
            val deferred = scope.async(Dispatchers.IO) {
                runCatching {
                    val insight = geminiRepository.generateInsight(
                        currentSteps = currentSteps,
                        stepGoal = stepGoal,
                        goalPercentage = goalPct,
                        timeOfDay = timeOfDay
                    )
                    val entry = Entry(
                        insight = insight,
                        generatedAtMillis = nowMillis,
                        stepsAt = currentSteps,
                        goalPctAt = goalPct
                    )
                    cache[key] = entry
                    maybeEvict()
                    insight
                }.getOrElse {
                    val ageOk = cached?.let { (nowMillis - it.generatedAtMillis) / 60000L < config.maxStalenessMinutes } == true
                    if (ageOk) cached!!.insight else FALLBACK_INSIGHT
                }
            }
            inFlight[key] = deferred
            deferred.invokeOnCompletion {
                synchronized(lock) { inFlight.remove(key) }
            }
            deferred
        }.await()
    }

    private fun toDateKey(nowMillis: Long): String {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = nowMillis
        val y = cal.get(java.util.Calendar.YEAR)
        val m = cal.get(java.util.Calendar.MONTH) + 1
        val d = cal.get(java.util.Calendar.DAY_OF_MONTH)
        return String.format(java.util.Locale.US, "%04d-%02d-%02d", y, m, d)
    }

    private fun timeBucketOf(timeOfDay: String): String {
        return when (timeOfDay.lowercase(java.util.Locale.ROOT)) {
            "morning", "afternoon", "evening", "night" -> timeOfDay.lowercase(java.util.Locale.ROOT)
            else -> "day"
        }
    }

    private fun bucket(goalPct: Int): Int = when {
        goalPct >= 100 -> 100
        goalPct >= 75 -> 75
        goalPct >= 50 -> 50
        goalPct >= 25 -> 25
        else -> 0
    }

    private fun maybeEvict() {
        // Simple eviction: keep only the most recent N entries
        if (cache.size <= config.maxCacheEntries) return
        val entries = cache.entries.toList()
        val sorted = entries.sortedByDescending { it.value.generatedAtMillis }
        val keep = sorted.take(config.maxCacheEntries).associate { it.key to it.value }
        cache.clear()
        cache.putAll(keep)
    }
}
