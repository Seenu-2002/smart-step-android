package com.seenu.dev.android.smartstep.ai_coach.data

interface AiCoachRepository {
    suspend fun generateInsight(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String

    fun createChatSession(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    )

    suspend fun generateGreeting(): String

    suspend fun generateGreeting(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String

    suspend fun sendMessage(userMessage: String): String

    suspend fun sendMessage(
        userMessage: String,
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String
}

data class ChatContext(
    val currentSteps: Int,
    val stepGoal: Int,
    val goalPercentage: Int,
    val timeOfDay: String
)
