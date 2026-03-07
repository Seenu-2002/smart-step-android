package com.seenu.dev.android.smartstep.home.home_presentation

data class HomeState constructor(
    val isFirstInstall: Boolean = false,
    val permissionDenialStep: DenialStep? = null,
    val showBackgroundAccessRecommended: Boolean = false,
    val activityRecognitionPermissionGranted: Boolean = false,
    val isIgnoringBatteryOptimizations: Boolean = false,
    val showExitConfirmationDialog: Boolean = false,
    val showStepGoalSheet: Boolean = false,

    // StepCounterCard state
    val currentSteps: Int = 0,
    val stepGoal: Int = 0,
    val isPaused: Boolean = false,
    val distanceText: String = "0.0",
    val caloriesText: String = "0",
    val minutesText: String = "0",
    val isMetric: Boolean = true,

    // EditStepDialog state
    val showEditStepsDialog: Boolean = false
)
