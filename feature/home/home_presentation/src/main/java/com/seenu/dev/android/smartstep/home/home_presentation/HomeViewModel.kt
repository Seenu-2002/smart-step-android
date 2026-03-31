package com.seenu.dev.android.smartstep.home.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.smartstep.ai_coach.data.AiInsightsRepository
import com.seenu.dev.android.smartstep.ai_coach.data.TimeOfDayProvider
import com.seenu.dev.android.smartstep.ai_coach.presentation.models.AiInsightState
import com.seenu.dev.android.smartstep.design_system.components.DailyAverageStepsCardData
import com.seenu.dev.android.smartstep.design_system.components.StepsPerDayData
import com.seenu.dev.android.smartstep.domain.connectivity.ConnectivityObserver
import com.seenu.dev.android.smartstep.domain.extensions.toCentimeters
import com.seenu.dev.android.smartstep.domain.model.Gender
import com.seenu.dev.android.smartstep.domain.model.HeightMetric
import com.seenu.dev.android.smartstep.domain.model.UserConfig
import com.seenu.dev.android.smartstep.domain.model.WeightMetric
import com.seenu.dev.android.smartstep.domain.repository.PermissionRepository
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import com.seenu.dev.android.smartstep.home.home_domain.BatteryOptimizationRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import com.seenu.dev.android.smartstep.home.home_domain.StepMetricsCalculator
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import com.seenu.dev.android.smartstep.home.home_presentation.models.MetricsDataUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
typealias DesignSystemString = com.seenu.dev.android.core.design_system.R.string

class HomeViewModel(
    private val permissionRepository: PermissionRepository,
    private val preferenceManager: PreferenceManager,
    private val batteryOptimizationRepository: BatteryOptimizationRepository,
    private val stepRepository: StepRepository,
    private val userConfigRepository: UserConfigRepository,
    private val aiInsightsRepository: AiInsightsRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var lastCalculatedSteps = 0

    // AI Insight tracking
    private var hasGeneratedInitialInsight = false
    private var previousGoalReached = false
    private var previousStepGoal = 0

    init {
        checkActivityRecognitionPermission()
        observeUserConfig()

        observePauseState()
        observeStepsAndCalculate()
        observeDailyAverageSteps()
        observeConnectivity()
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
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    stepRepository.updateStepGoal(homeAction.stepGoal, today)
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
                    stepRepository.updateStepsManually(homeAction.steps, homeAction.date)

                    // Close the dialog after saving
                    _uiState.update { it.copy(showEditStepsDialog = false) }
                }
            }

            // AI Insights Actions
            HomeAction.OnAiInsightTryAgain -> {
                viewModelScope.launch {
                    val currentlyOnline = connectivityObserver.isOnline.first()
                    _uiState.update { it.copy(isOnline = currentlyOnline) }
                    if (currentlyOnline) {
                        refreshAiInsight()
                    }
                }
            }

            HomeAction.OnLifecycleResume -> {
                refreshAiInsight()
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
                val oldGoal = _uiState.value.stepGoal
                _uiState.update {
                    it.copy(
                        stepGoal = userConfig.targetStepCount
                    )
                }
                // Trigger 4: step goal changed
                if (oldGoal != 0 && oldGoal != userConfig.targetStepCount) {
                    previousStepGoal = userConfig.targetStepCount
                    refreshAiInsight()
                }
            }
        }
    }

    private fun observeDailyAverageSteps() {
        viewModelScope.launch {
            // TODO: Should be injected via DI
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val today = formatter.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            val startingDate = formatter.format(calendar.time)
            stepRepository.getStepsForRangeFlow(startingDate, today).collect { stepsPerDay ->
                // Build a continuous 7-day range [start..today], fill missing days with 0 steps
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startCal = Calendar.getInstance().apply {
                    time = formatter.parse(startingDate) ?: Date()
                }
                val endCal = Calendar.getInstance().apply {
                    time = formatter.parse(today) ?: Date()
                }

                // Index existing results by date for quick lookup
                val byDate = stepsPerDay.associateBy { it.date }

                // Fallback goal if a particular day doesn't have one stored
                val fallbackGoal = _uiState.value.stepGoal

                val filled = mutableListOf<StepsPerDayData>()
                var cursor = startCal.clone() as Calendar
                var totalSteps = 0
                while (!cursor.after(endCal)) {
                    val dateStr = formatter.format(cursor.time)
                    val record = byDate[dateStr]
                    val steps = record?.steps ?: 0
                    val goal = record?.goal ?: fallbackGoal
                    totalSteps += steps

                    filled += StepsPerDayData(
                        dayLabelRes = getDayLabelResForDate(dateStr),
                        steps = steps,
                        goal = goal
                    )

                    cursor.add(Calendar.DAY_OF_YEAR, 1)
                }

                val averageStepsPerDay = if (filled.isNotEmpty()) totalSteps / filled.size else 0
                val data = DailyAverageStepsCardData(
                    averageStepsPerDay = averageStepsPerDay,
                    stepsPerDay = filled
                )

                _uiState.update {
                    it.copy(dailyAverageStepsCardData = data)
                }
            }
        }
    }
    
    // TODO: This has to be injected from DI and move this function into a util
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private fun getDayLabelResForDate(label: String): Int {
        val date = dateFormatter.parse(label)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY ->  DesignSystemString.day_sunday
            Calendar.MONDAY -> DesignSystemString.day_monday
            Calendar.TUESDAY -> DesignSystemString.day_tuesday
            Calendar.WEDNESDAY -> DesignSystemString.day_wednesday
            Calendar.THURSDAY -> DesignSystemString.day_thursday
            Calendar.FRIDAY -> DesignSystemString.day_friday
            Calendar.SATURDAY -> DesignSystemString.day_saturday
            else -> throw IllegalArgumentException("Invalid date: $label")
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
        combine(
            stepRepository.getTodaySteps(),
            stepRepository.getTodayActiveSeconds(),
            userConfigRepository.getUserConfigFlow()
        ) {currentSteps, activeSeconds, userConfig ->
            MetricsDataUi(currentSteps, activeSeconds, userConfig)
        }.onEach { (currentSteps, activeSeconds, userConfig) ->
            updateMetricsIfNeeded(currentSteps, activeSeconds, userConfig, forceUpdate = false)
        }.launchIn(viewModelScope)
    }

    private fun updateMetricsIfNeeded(
        currentSteps: Int,
        activeSeconds: Long,
        userConfig: UserConfig,
        forceUpdate: Boolean
    ) {
        val state = _uiState.value

        if (state.isPaused && !forceUpdate) return

        val stepDifference = kotlin.math.abs(currentSteps - lastCalculatedSteps)
        if (forceUpdate || stepDifference >= 10) {
            lastCalculatedSteps = currentSteps

            val heightCm = when (val height = userConfig.heightMetric) {
                is HeightMetric.Centimeters -> height.value
                is HeightMetric.FeetInches -> height.toCentimeters()
            }.toFloat()
            val weight = userConfig.weightMetric.getWeightValue().toFloat()
            val isWeightLbs = userConfig.weightMetric is WeightMetric.Pounds
            val isMale = userConfig.gender == Gender.MALE

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

            // Trigger 3: step count crosses daily goal
            val goal = state.stepGoal
            if (goal > 0) {
                val goalNowReached = currentSteps >= goal
                if (goalNowReached && !previousGoalReached) {
                    previousGoalReached = true
                    refreshAiInsight()
                } else if (!goalNowReached) {
                    previousGoalReached = false
                }
            }
        }
    }

    // --- AI Insight logic ---

    private fun observeConnectivity() {
        connectivityObserver.isOnline
            .onEach { online ->
                _uiState.update { it.copy(isOnline = online) }
                if (online && _uiState.value.aiInsightState is AiInsightState.Offline) {
                    refreshAiInsight()
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshAiInsight() {
        val state = _uiState.value
        if (!state.isOnline) {
            _uiState.update { it.copy(aiInsightState = AiInsightState.Offline) }
            return
        }

        _uiState.update { it.copy(aiInsightState = AiInsightState.Loading) }

        viewModelScope.launch {
            val goalPercentage = if (state.stepGoal > 0) {
                (state.currentSteps * 100) / state.stepGoal
            } else 0
            val timeOfDay = TimeOfDayProvider.getTimeOfDay()

            val insight = aiInsightsRepository.getInsight(
                currentSteps = state.currentSteps,
                stepGoal = state.stepGoal,
                timeOfDay = timeOfDay
            )

            _uiState.update {
                it.copy(aiInsightState = AiInsightState.Success(insight))
            }
            hasGeneratedInitialInsight = true
        }
    }
}