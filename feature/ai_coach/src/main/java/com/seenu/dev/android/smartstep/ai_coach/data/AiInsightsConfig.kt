package com.seenu.dev.android.smartstep.ai_coach.data

data class AiInsightsConfig(
    val ttlMinutes: Long = 15,
    val stepDeltaThreshold: Int = 250,
    val maxStalenessMinutes: Long = 180,
    val maxCacheEntries: Int = 50
)
