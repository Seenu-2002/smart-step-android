package com.seenu.dev.android.smartstep.home.home_presentation

data class HomeState(
    val isFirstInstall: Boolean = false,
    val permissionDenialStep: DenialStep? = null,
    val showBackgroundAccessRecommended: Boolean = false,
    val activityRecognitionPermissionGranted: Boolean = false,
    val isIgnoringBatteryOptimizations: Boolean = false
)
