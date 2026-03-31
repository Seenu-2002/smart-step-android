package com.seenu.dev.android.smartstep.ai_coach.data

interface AiInsightsRepository {
    suspend fun getInsight(
        currentSteps: Int,
        stepGoal: Int,
        timeOfDay: String,
        nowMillis: Long = System.currentTimeMillis()
    ): String

    fun forceRefresh()
}
