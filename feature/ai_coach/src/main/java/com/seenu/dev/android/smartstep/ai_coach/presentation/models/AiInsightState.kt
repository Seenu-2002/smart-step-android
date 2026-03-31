package com.seenu.dev.android.smartstep.ai_coach.presentation.models

import androidx.compose.runtime.Stable

@Stable
sealed interface AiInsightState {
    data object Loading : AiInsightState
    data class Success(val message: String) : AiInsightState
    data class Error(val message: String) : AiInsightState
    data object Offline : AiInsightState
}
