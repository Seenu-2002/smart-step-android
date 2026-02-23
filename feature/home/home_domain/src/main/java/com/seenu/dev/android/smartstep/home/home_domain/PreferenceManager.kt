package com.seenu.dev.android.smartstep.home.home_domain

import kotlinx.coroutines.flow.Flow
interface PreferenceManager {

    val isFirstInstall: Flow<Boolean>

    val activityRecognitionRationaleShowed: Flow<Boolean>

    val backgroundPermissionRequired: Flow<Boolean>

    suspend fun markFirstInstallCompleted()

    suspend fun markActivityRecognitionRationaleShowed()

    suspend fun markBackgroundPermissionRequired()

}