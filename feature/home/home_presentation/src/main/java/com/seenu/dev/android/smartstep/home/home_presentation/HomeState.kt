package com.seenu.dev.android.smartstep.home.home_presentation

data class HomeState(
    val isFirstInstall: Boolean = false,
    val permissionDenialStep: DenialStep? = null,
    val activityRecognitionPermissionGranted: Boolean = false
)
