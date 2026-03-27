package com.seenu.dev.android.smartstep.ai_coach.data

import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.seenu.dev.android.smartstep.ai_coach.BuildConfig

class GeminiRepository {

    private val insightModel = GenerativeModel(
        // Use a currently supported Flash model. 2.0-flash is deprecated for new users; switch to 2.5-flash.
        modelName = "gemini-2.5-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.5F
            // Short, single response — keep generous but compact to avoid truncation
            maxOutputTokens = 500
            // Ensure plain text response
            responseMimeType = "text/plain"
        },
        systemInstruction = content {
            text(INSIGHT_SYSTEM_PROMPT)
        }
    )

    private val chatModel = GenerativeModel(
        modelName = "gemini-2.5-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.5F
            maxOutputTokens = 500
            responseMimeType = "text/plain"
        },
        systemInstruction = content {
            text(CHAT_SYSTEM_PROMPT)
        }
    )

    private var currentChat: Chat? = null
    private var lastContext: ChatContext? = null

    suspend fun generateInsight(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String {
        return try {
            val prompt = buildInsightPrompt(currentSteps, stepGoal, goalPercentage, timeOfDay)
            val response = insightModel.generateContent(prompt)
            response.text?.trim() ?: FALLBACK_INSIGHT
        } catch (e: Exception) {
            FALLBACK_INSIGHT
        }
    }

    fun createChatSession(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ) {
        ensureChatSession(
            ChatContext(
                currentSteps = currentSteps,
                stepGoal = stepGoal,
                goalPercentage = goalPercentage,
                timeOfDay = timeOfDay
            )
        )
    }

    suspend fun generateGreeting(): String {
        return try {
            val chat = currentChat ?: return FALLBACK_GREETING
            val response = chat.sendMessage(GREETING_PROMPT)
            response.text?.trim() ?: FALLBACK_GREETING
        } catch (e: Exception) {
            FALLBACK_GREETING
        }
    }

    suspend fun generateGreeting(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String {
        return try {
            ensureChatSession(
                ChatContext(currentSteps, stepGoal, goalPercentage, timeOfDay)
            )
            val chat = currentChat ?: return FALLBACK_GREETING
            val response = chat.sendMessage(GREETING_PROMPT)
            response.text?.trim() ?: FALLBACK_GREETING
        } catch (e: Exception) {
            FALLBACK_GREETING
        }
    }

    suspend fun sendMessage(userMessage: String): String {
        return try {
            val chat = currentChat ?: return FALLBACK_RESPONSE
            val response = chat.sendMessage(userMessage)
            response.text?.trim() ?: FALLBACK_RESPONSE
        } catch (e: Exception) {
            FALLBACK_RESPONSE
        }
    }

    suspend fun sendMessage(
        userMessage: String,
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String {
        return try {
            ensureChatSession(
                ChatContext(currentSteps, stepGoal, goalPercentage, timeOfDay)
            )
            val chat = currentChat ?: return FALLBACK_RESPONSE
            val response = chat.sendMessage(userMessage)
            response.text?.trim() ?: FALLBACK_RESPONSE
        } catch (e: Exception) {
            FALLBACK_RESPONSE
        }
    }

    private fun buildInsightPrompt(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String {
        return """
            User's activity context:
            - Current step count: $currentSteps
            - Daily step goal: $stepGoal
            - Goal completion: $goalPercentage%
            - Time of day: $timeOfDay

            Generate a short motivational insight about the user's activity.
        """.trimIndent()
    }

    private fun buildChatContextMessage(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String {
        return """
            [SYSTEM CONTEXT - Do not repeat these numbers directly]
            User's current activity data:
            - Current step count: $currentSteps
            - Daily step goal: $stepGoal
            - Goal completion: $goalPercentage%
            - Time of day: $timeOfDay

            Use this context to inform your responses but do not quote raw numbers back to the user.
        """.trimIndent()
    }

    private fun ensureChatSession(context: ChatContext) {
        if (lastContext == null || lastContext != context) {
            val contextMessage = buildChatContextMessage(
                context.currentSteps,
                context.stepGoal,
                context.goalPercentage,
                context.timeOfDay
            )
            currentChat = chatModel.startChat(
                history = listOf(
                    content(role = "user") { text(contextMessage) }
                )
            )
            lastContext = context
        }
    }

    companion object {
        private const val INSIGHT_SYSTEM_PROMPT = """
            You are a concise AI fitness coach embedded in a step counter app.
            Rules:
            - Return exactly one short message (max 2 sentences)
            - Interpret the user's current activity state with a motivational or analytical tone
            - Do NOT include medical advice
            - Do NOT repeat raw numeric values from the context
            - Do NOT ask questions
            - Be encouraging and specific to their progress level
        """

        private const val CHAT_SYSTEM_PROMPT = """
            You are a friendly AI fitness coach in a step counter app.
            Rules:
            - Be encouraging and helpful
            - Give practical fitness advice related to walking and daily activity
            - Do NOT give medical advice or diagnoses
            - Do NOT repeat raw numeric values from the user's activity context
            - Keep responses concise (2-4 sentences unless more detail is requested)
            - Be conversational and warm
        """

        private const val GREETING_PROMPT = """
            Generate a short welcome greeting as the user's AI fitness coach.
            Rules:
            - Welcome the user
            - Briefly acknowledge their current activity level (without quoting exact numbers)
            - Ask how you can help
            - Maximum 4 sentences
            - No medical advice, no raw numbers
        """

        private const val FALLBACK_INSIGHT = "Keep moving! Every step brings you closer to your goal."
        private const val FALLBACK_GREETING = "Hello! I'm your AI fitness coach. I'm here to help you stay on track with your daily activity goals. How can I help you today?"
        private const val FALLBACK_RESPONSE = "I'm sorry, I couldn't process that right now. Please try again in a moment."
    }
}

data class ChatContext(
    val currentSteps: Int,
    val stepGoal: Int,
    val goalPercentage: Int,
    val timeOfDay: String
)
