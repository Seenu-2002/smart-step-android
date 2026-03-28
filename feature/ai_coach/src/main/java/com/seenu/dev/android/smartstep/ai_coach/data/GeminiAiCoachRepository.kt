package com.seenu.dev.android.smartstep.ai_coach.data

import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.seenu.dev.android.smartstep.ai_coach.BuildConfig
import kotlinx.coroutines.CancellationException

class GeminiAiCoachRepository : AiCoachRepository {

    private val insightModel = GenerativeModel(
        modelName = MODEL_NAME,
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = TEMPERATURE
            maxOutputTokens = MAX_OUTPUT_TOKENS
            responseMimeType = RESPONSE_MIME_TYPE
        },
        systemInstruction = content {
            text(INSIGHT_SYSTEM_PROMPT)
        }
    )

    private val chatModel = GenerativeModel(
        modelName = MODEL_NAME,
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = TEMPERATURE
            maxOutputTokens = MAX_OUTPUT_TOKENS
            responseMimeType = RESPONSE_MIME_TYPE
        },
        systemInstruction = content {
            text(CHAT_SYSTEM_PROMPT)
        }
    )

    private var currentChat: Chat? = null
    private var lastContext: ChatContext? = null

    override suspend fun generateInsight(
        currentSteps: Int,
        stepGoal: Int,
        goalPercentage: Int,
        timeOfDay: String
    ): String {
        return try {
            val prompt = buildInsightPrompt(currentSteps, stepGoal, goalPercentage, timeOfDay)
            val response = insightModel.generateContent(prompt)
            response.text?.trim() ?: FALLBACK_INSIGHT
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            FALLBACK_INSIGHT
        }
    }

    override fun createChatSession(
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

    override suspend fun generateGreeting(): String {
        return try {
            val chat = currentChat ?: return FALLBACK_GREETING
            val response = chat.sendMessage(GREETING_PROMPT)
            response.text?.trim() ?: FALLBACK_GREETING
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            FALLBACK_GREETING
        }
    }

    override suspend fun generateGreeting(
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            FALLBACK_GREETING
        }
    }

    override suspend fun sendMessage(userMessage: String): String {
        return try {
            val chat = currentChat ?: return FALLBACK_RESPONSE
            val response = chat.sendMessage(userMessage)
            response.text?.trim() ?: FALLBACK_RESPONSE
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            FALLBACK_RESPONSE
        }
    }

    override suspend fun sendMessage(
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
        } catch (e: CancellationException) {
            throw e
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
        private const val MODEL_NAME = "gemini-2.5-flash-lite"
        private const val TEMPERATURE = 0.5F
        private const val MAX_OUTPUT_TOKENS = 500
        private const val RESPONSE_MIME_TYPE = "text/plain"

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
