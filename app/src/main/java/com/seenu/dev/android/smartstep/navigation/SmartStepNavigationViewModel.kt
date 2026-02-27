package com.seenu.dev.android.smartstep.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmartStepNavigationViewModel constructor(
    private val userConfigRepository: UserConfigRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<NavigationState> = MutableStateFlow(NavigationState())
    val uiState: StateFlow<NavigationState> = _uiState.asStateFlow()

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            val userConfig = userConfigRepository.getUserConfig()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    startRoute = if (userConfig.isFirstSetupCompleted) {
                        Route.StepCounterScreen
                    } else {
                        Route.ProfileSetupScreen
                    }
                )
            }
        }
    }

}

data class NavigationState constructor(
    val isLoading: Boolean = true,
    val startRoute: Route = Route.ProfileSetupScreen
)