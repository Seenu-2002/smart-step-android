package com.seenu.dev.android.smartstep.ai_coach.presentation

sealed interface AiCoachAction {
    data class SendMessage(val message: String) : AiCoachAction
    data object ToggleSuggestions : AiCoachAction
    data class UpdateInput(val text: String) : AiCoachAction
}
