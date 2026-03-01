package com.seenu.dev.android.smartstep.profile_setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.smartstep.design_system.components.HeightMetricUiModel
import com.seenu.dev.android.smartstep.design_system.components.WeightMetricUiModel
import com.seenu.dev.android.smartstep.domain.model.Gender
import com.seenu.dev.android.smartstep.domain.model.UserConfig
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import com.seenu.dev.android.smartstep.profile_setup.mapper.toDomainModel
import com.seenu.dev.android.smartstep.profile_setup.mapper.toUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileSetupScreenViewModel constructor(
    private val userConfigRepository: UserConfigRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProfileSetupScreenUiState> =
        MutableStateFlow(ProfileSetupScreenUiState())
    val uiState: StateFlow<ProfileSetupScreenUiState> = _uiState.onStart {
        observeUserConfig()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileSetupScreenUiState()
    )

    private val _sideEffects: MutableSharedFlow<ProfileSetupSideEffect> = MutableSharedFlow()
    val sideEffect = _sideEffects.asSharedFlow()

    private fun observeUserConfig() {
        viewModelScope.launch {
            userConfigRepository.getUserConfigFlow().collectLatest { userConfig ->
                _uiState.update {
                    it.copy(
                        heightMetric = userConfig.heightMetric.toUiModel(),
                        weightMetric = userConfig.weightMetric.toUiModel(),
                        gender = userConfig.gender
                    )
                }
            }
        }
    }

    fun onIntent(intent: ProfileSetupScreenIntent) {
        when (intent) {
            is ProfileSetupScreenIntent.ShowGenderSelectionDropDown -> {
                _uiState.value = _uiState.value.copy(showGenderDropDown = true)
            }

            is ProfileSetupScreenIntent.HideGenderSelectionDropDown -> {
                _uiState.value = _uiState.value.copy(showGenderDropDown = false)
            }

            is ProfileSetupScreenIntent.ShowHeightSelectionCard -> {
                _uiState.value = _uiState.value.copy(showHeightSelectionCard = true)
            }

            is ProfileSetupScreenIntent.HideHeightSelectionCard -> {
                _uiState.value = _uiState.value.copy(showHeightSelectionCard = false)
            }

            is ProfileSetupScreenIntent.ShowWeightSelectionCard -> {
                _uiState.value = _uiState.value.copy(showWeightSelectionCard = true)
            }

            is ProfileSetupScreenIntent.HideWeightSelectionCard -> {
                _uiState.value = _uiState.value.copy(showWeightSelectionCard = false)
            }

            is ProfileSetupScreenIntent.OnGenderSelected -> {
                _uiState.value =
                    _uiState.value.copy(gender = intent.gender, showGenderDropDown = false)
            }

            is ProfileSetupScreenIntent.OnHeightSelected -> {
                _uiState.value = _uiState.value.copy(
                    heightMetric = intent.heightMetric
                )
            }

            is ProfileSetupScreenIntent.OnWeightSelected -> {
                _uiState.value = _uiState.value.copy(
                    weightMetric = intent.weightMetric
                )
            }

            is ProfileSetupScreenIntent.OnCompleteProfileSetup -> {
                completeRegistration()
            }

            is ProfileSetupScreenIntent.OnSkip -> {
                skipOnboarding()
            }
        }
    }

    private fun skipOnboarding() {
        viewModelScope.launch {
            val defaultState = ProfileSetupScreenUiState()
            userConfigRepository.updateUserConfig(
                UserConfig(
                    gender = defaultState.gender,
                    isFirstSetupCompleted = true,
                    heightMetric = defaultState.heightMetric.toDomainModel(),
                    weightMetric = defaultState.weightMetric.toDomainModel(),
                )
            )
            _sideEffects.emit(ProfileSetupSideEffect.OnProfileSetupComplete)
        }
    }

    private fun completeRegistration() {
        viewModelScope.launch {
            val data = _uiState.value
            userConfigRepository.updateUserConfig(
                UserConfig(
                    gender = data.gender,
                    isFirstSetupCompleted = true,
                    heightMetric = data.heightMetric.toDomainModel(),
                    weightMetric = data.weightMetric.toDomainModel()
                )
            )
            _sideEffects.emit(ProfileSetupSideEffect.OnProfileSetupComplete)
        }
    }
}

data class ProfileSetupScreenUiState(
    val gender: Gender = Gender.FEMALE,
    val heightMetric: HeightMetricUiModel = HeightMetricUiModel.Centimeters(170),
    val weightMetric: WeightMetricUiModel = WeightMetricUiModel.Kilograms(70),
    val showGenderDropDown: Boolean = false,
    val showHeightSelectionCard: Boolean = false,
    val showWeightSelectionCard: Boolean = false,
)

sealed interface ProfileSetupScreenIntent {
    data object ShowGenderSelectionDropDown : ProfileSetupScreenIntent
    data object HideGenderSelectionDropDown : ProfileSetupScreenIntent
    data object ShowHeightSelectionCard : ProfileSetupScreenIntent
    data object HideHeightSelectionCard : ProfileSetupScreenIntent
    data object ShowWeightSelectionCard : ProfileSetupScreenIntent
    data object HideWeightSelectionCard : ProfileSetupScreenIntent
    data class OnGenderSelected(val gender: Gender) : ProfileSetupScreenIntent
    data class OnHeightSelected(val heightMetric: HeightMetricUiModel) : ProfileSetupScreenIntent
    data class OnWeightSelected(val weightMetric: WeightMetricUiModel) : ProfileSetupScreenIntent
    data object OnCompleteProfileSetup : ProfileSetupScreenIntent
    data object OnSkip : ProfileSetupScreenIntent
}

sealed interface ProfileSetupSideEffect {
    data object OnProfileSetupComplete : ProfileSetupSideEffect
}