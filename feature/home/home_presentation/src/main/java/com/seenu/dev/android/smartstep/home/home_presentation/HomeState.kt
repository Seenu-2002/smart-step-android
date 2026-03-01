package com.seenu.dev.android.smartstep.home.home_presentation

data class HomeState constructor(
    val isFirstInstall: Boolean = false,
    val permissionDenialStep: DenialStep? = null,
    val showBackgroundAccessRecommended: Boolean = false,
    val activityRecognitionPermissionGranted: Boolean = false,
    val isIgnoringBatteryOptimizations: Boolean = false,
    val showExitConfirmationDialog: Boolean = false,
    val showStepGoalSheet: Boolean = false,
    val currentSteps: Int = 1340,
    val stepGoal: Int = 3000,
)
