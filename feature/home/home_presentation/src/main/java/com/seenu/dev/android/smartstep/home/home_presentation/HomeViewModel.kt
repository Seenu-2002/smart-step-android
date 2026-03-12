package com.seenu.dev.android.smartstep.home.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.smartstep.domain.repository.PermissionRepository
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import com.seenu.dev.android.smartstep.home.home_domain.BatteryOptimizationRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import com.seenu.dev.android.smartstep.home.home_domain.StepMetricsCalculator
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val permissionRepository: PermissionRepository,
    private val preferenceManager: PreferenceManager,
    private val batteryOptimizationRepository: BatteryOptimizationRepository,
    private val stepRepository: StepRepository,
    private val userConfigRepository: UserConfigRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var lastCalculatedSteps = 0

    init {
        checkActivityRecognitionPermission()
        observeUserConfig()

        observePauseState()
        observeStepsAndCalculate()
    }

    fun onAction(homeAction: HomeAction) {
        when (homeAction) {
            is HomeAction.OnActivityRecognitionPermissionUpdate -> {
                _uiState.update {
                    it.copy(activityRecognitionPermissionGranted = homeAction.granted)
                }
                if (!homeAction.granted) {
                    _uiState.update {
                        val nextStep = increaseDenialStep(it.permissionDenialStep)
                        it.copy(permissionDenialStep = nextStep)
                    }
                } else {
                    checkBackgroundPermission()
                }
            }

            HomeAction.OnRequireActivityRecognitionPermission -> sendActivityRecognitionPermissionRequiredEvent()

            HomeAction.RationaleRequired -> {
                viewModelScope.launch {
                    preferenceManager.markActivityRecognitionRationaleShowed()
                }
            }

            HomeAction.OnPermissionUpdateRequired -> {
                val hasPermission = permissionRepository.hasActivityRecognitionPermission()
                _uiState.update {
                    it.copy(activityRecognitionPermissionGranted = hasPermission)
                }
                if (hasPermission) {
                    checkBackgroundPermission()
                }
            }

            HomeAction.OnBackgroundAccessRecommendedDismiss -> {
                _uiState.update { it.copy(showBackgroundAccessRecommended = false) }
                viewModelScope.launch {
                    preferenceManager.markBackgroundPermissionRequired()
                }
            }

            HomeAction.OnBackgroundAccessRecommendedContinue -> {
                _uiState.update { it.copy(showBackgroundAccessRecommended = false) }
                viewModelScope.launch {
                    preferenceManager.markBackgroundPermissionRequired()
                    eventChannel.send(HomeEvent.OnBackgroundPermissionRequired)
                }
            }

            HomeAction.CheckIsIgnoringBatteryOptimizations -> {
                _uiState.update {
                    it.copy(
                        isIgnoringBatteryOptimizations = batteryOptimizationRepository.isIgnoringBatteryOptimizations()
                    )
                }
            }

            HomeAction.OnFixStopCountingStepIssueClick -> {
                _uiState.update {
                    it.copy(
                        showBackgroundAccessRecommended = true
                    )
                }
            }

            HomeAction.ShowExitConfirmationDialog -> {
                _uiState.update {
                    it.copy(showExitConfirmationDialog = true)
                }
            }

            HomeAction.DismissExitConfirmationDialog -> {
                _uiState.update {
                    it.copy(showExitConfirmationDialog = false)
                }
            }

            HomeAction.ShowStepGoalSheet -> {
                _uiState.update {
                    it.copy(showStepGoalSheet = true)
                }
            }

            HomeAction.DismissStepGoalSheet -> {
                _uiState.update {
                    it.copy(showStepGoalSheet = false)
                }
            }

            is HomeAction.UpdateStepGoal -> {
                viewModelScope.launch {
                    userConfigRepository.updateTargetStepCount(homeAction.stepGoal)
                    _uiState.update {
                        it.copy(stepGoal = homeAction.stepGoal, showStepGoalSheet = false)
                    }
                }
            }

            // StepCounterCard Action
            HomeAction.OnPausePlayIconClick ->  {
                viewModelScope.launch {
                    val currentState = _uiState.value.isPaused
                    preferenceManager.setStepTrackingPaused(!currentState)
                }
            }

            // Currently just changing state no UI
            HomeAction.OnEditStepsClick ->  {
                _uiState.update { it.copy(showEditStepsDialog = true) }
            }

            HomeAction.DismissEditStepsDialog -> {
                _uiState.update { it.copy(showEditStepsDialog = false) }
            }

            is HomeAction.OnSubmitEditedSteps -> { // (Assuming you rename OnUserTake10Steps to this)
                viewModelScope.launch {
                    // Save to Room DB. The 'combine' flow above will automatically
                    // detect this change and force the UI to recalculate!
                    stepRepository.updateStepsManually(homeAction.steps)

                    // Close the dialog after saving
                    _uiState.update { it.copy(showEditStepsDialog = false) }
                }
            }
        }
    }

    private fun checkBackgroundPermission() {
        viewModelScope.launch {
            if (!preferenceManager.backgroundPermissionRequired.first()) {
                _uiState.update { it.copy(showBackgroundAccessRecommended = true) }
            }
        }
    }

    private fun checkActivityRecognitionPermission() {
        viewModelScope.launch {
            val hasPermission = permissionRepository.hasActivityRecognitionPermission()
            _uiState.update {
                it.copy(activityRecognitionPermissionGranted = hasPermission)
            }
            if (!hasPermission) {
                if (preferenceManager.activityRecognitionRationaleShowed.first()) {
                    _uiState.update {
                        it.copy(permissionDenialStep = DenialStep.SECOND_DENIAL)
                    }
                } else {
                    sendActivityRecognitionPermissionRequiredEvent()
                }
            }
        }
    }

    private fun observeUserConfig() {
        viewModelScope.launch {
            userConfigRepository.getUserConfigFlow().collect { userConfig ->
                _uiState.update {
                    it.copy(
                        stepGoal = userConfig.targetStepCount
                    )
                }
            }
        }
    }

    private fun sendActivityRecognitionPermissionRequiredEvent() {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OnActivityRecognitionPermissionRequired)
        }
    }

    private fun checkIsFirstInstall() {
        viewModelScope.launch {
            val isFirstInstall = preferenceManager.isFirstInstall.first()
            _uiState.value = _uiState.value.copy(isFirstInstall = isFirstInstall)
            if (isFirstInstall) preferenceManager.markFirstInstallCompleted()
        }
    }

    private fun observePauseState() {
        viewModelScope.launch {
            preferenceManager.isStepTrackingPaused.collect { isPaused ->
                _uiState.update { it.copy(isPaused = isPaused) }
            }
        }
    }

    // Assume you have a flow observing real-time steps from a DB or Service
    private fun observeStepsAndCalculate() {
        viewModelScope.launch {
            // Combine listens to both flows from your Repository
            combine(
                stepRepository.getTodaySteps(),
                stepRepository.getTodayActiveSeconds()
            ) { steps, activeSeconds ->
                Pair(steps, activeSeconds)
            }.collect { (currentSteps, activeSeconds) ->
                // Pass the activeSeconds into your update function
                updateMetricsIfNeeded(currentSteps, activeSeconds, forceUpdate = false)
            }
        }
    }

    private fun updateMetricsIfNeeded(currentSteps: Int, activeSeconds: Long, forceUpdate: Boolean) {
        val state = _uiState.value

        if (state.isPaused && !forceUpdate) return

        val stepDifference = kotlin.math.abs(currentSteps - lastCalculatedSteps)
        if (forceUpdate || stepDifference >= 10) {
            lastCalculatedSteps = currentSteps

            // TODO: Hardcoded, Replace these with actual values from state
            val heightCm = 175f
            val weight = 70f
            val isWeightLbs = false
            val isMale = true

            val distance = StepMetricsCalculator.calculateDistance(currentSteps, heightCm, state.isMetric)
            val calories = StepMetricsCalculator.calculateCalories(currentSteps, weight, isWeightLbs, isMale)
            val minutes = StepMetricsCalculator.calculateActiveMinutes(activeSeconds)

            _uiState.update {
                it.copy(
                    currentSteps = currentSteps,
                    distanceText = String.format(java.util.Locale.getDefault(), "%.1f", distance),
                    caloriesText = calories.toString(),
                    minutesText = minutes.toString()
                )
            }
        }
    }
}