package com.seenu.dev.android.smartstep.home.home_presentation

sealed interface HomeEvent {
    object OnActivityRecognitionPermissionRequired: HomeEvent
    object OnBackgroundPermissionRequired: HomeEvent
}