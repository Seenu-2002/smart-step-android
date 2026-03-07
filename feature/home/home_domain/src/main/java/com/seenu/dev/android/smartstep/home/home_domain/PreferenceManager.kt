package com.seenu.dev.android.smartstep.home.home_domain

import kotlinx.coroutines.flow.Flow

interface PreferenceManager {

    // I make it more readable at least to me

    val isFirstInstall: Flow<Boolean>
    suspend fun markFirstInstallCompleted()

    val activityRecognitionRationaleShowed: Flow<Boolean>
    suspend fun markActivityRecognitionRationaleShowed()

    val backgroundPermissionRequired: Flow<Boolean>
    suspend fun markBackgroundPermissionRequired()

    val isStepTrackingPaused: Flow<Boolean>
    suspend fun setStepTrackingPaused(isPaused: Boolean)
}