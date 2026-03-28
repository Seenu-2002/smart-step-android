package com.seenu.dev.android.smartstep.ai_coach.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.smartstep.ai_coach.data.AiCoachRepository
import com.seenu.dev.android.smartstep.ai_coach.data.TimeOfDayProvider
import com.seenu.dev.android.smartstep.ai_coach.presentation.models.ChatMessage
import com.seenu.dev.android.smartstep.domain.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiCoachViewModel(
    private val aiCoachRepository: AiCoachRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val currentSteps: Int,
    private val stepGoal: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiCoachState())
    val uiState: StateFlow<AiCoachState> = _uiState.asStateFlow()

    private var sessionInitialized = false

    init {
        observeConnectivity()
    }

    fun onAction(action: AiCoachAction) {
        when (action) {
            is AiCoachAction.SendMessage -> sendMessage(action.message)
            AiCoachAction.ToggleSuggestions -> {
                _uiState.update { it.copy(isSuggestionsExpanded = !it.isSuggestionsExpanded) }
            }
            is AiCoachAction.UpdateInput -> {
                _uiState.update { it.copy(inputText = action.text) }
            }
        }
    }

    private fun observeConnectivity() {
        connectivityObserver.isOnline
            .onEach { online ->
                _uiState.update { it.copy(isOnline = online) }
                // Initialize chat session and greeting once we come online
                if (online && !sessionInitialized) {
                    initializeSession()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun initializeSession() {
        // Guard: only initialize when online
        if (!_uiState.value.isOnline) return

        val goalPercentage = if (stepGoal > 0) (currentSteps * 100) / stepGoal else 0
        val timeOfDay = TimeOfDayProvider.getTimeOfDay()

        // Generate greeting with up-to-date context (ensures chat session is created/refreshed)
        viewModelScope.launch {
            sessionInitialized = true
            _uiState.update {
                it.copy(
                    messages = listOf(ChatMessage(text = "", isFromUser = false, isLoading = true)),
                    isAiResponding = true
                )
            }

            val greeting = aiCoachRepository.generateGreeting(
                currentSteps = currentSteps,
                stepGoal = stepGoal,
                goalPercentage = goalPercentage,
                timeOfDay = timeOfDay
            )

            _uiState.update {
                it.copy(
                    messages = listOf(ChatMessage(text = greeting, isFromUser = false)),
                    isAiResponding = false
                )
            }
        }
    }

    private fun sendMessage(text: String) {
        if (text.isBlank() || !_uiState.value.isOnline || _uiState.value.isAiResponding) return

        val userMessage = ChatMessage(text = text.trim(), isFromUser = true)
        val loadingMessage = ChatMessage(text = "", isFromUser = false, isLoading = true)

        _uiState.update {
            it.copy(
                messages = it.messages + userMessage + loadingMessage,
                isAiResponding = true
            )
        }

        viewModelScope.launch {
            val goalPercentage = if (stepGoal > 0) (currentSteps * 100) / stepGoal else 0
            val timeOfDay = TimeOfDayProvider.getTimeOfDay()
            val response = aiCoachRepository.sendMessage(
                userMessage = text.trim(),
                currentSteps = currentSteps,
                stepGoal = stepGoal,
                goalPercentage = goalPercentage,
                timeOfDay = timeOfDay
            )

            _uiState.update {
                val updatedMessages = it.messages.dropLast(1) +
                        ChatMessage(text = response, isFromUser = false)
                it.copy(
                    messages = updatedMessages,
                    isAiResponding = false,
                    inputText = ""
                )
            }
        }
    }
}
