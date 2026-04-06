package com.seenu.dev.android.smartstep.ai_coach.presentation

import com.seenu.dev.android.smartstep.ai_coach.presentation.models.ChatMessage

data class AiCoachState(
    val messages: List<ChatMessage> = emptyList(),
    val isOnline: Boolean = false,
    val isSuggestionsExpanded: Boolean = true,
    val isAiResponding: Boolean = false,
    val inputText: String = ""
)
