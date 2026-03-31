package com.seenu.dev.android.smartstep.ai_coach.presentation.models

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val isLoading: Boolean = false
)
