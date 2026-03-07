package com.seenu.dev.android.smartstep.home.home_data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")


class PreferenceManagerImpl(
    private val dataStore: DataStore<Preferences>
): PreferenceManager {

    private object Keys {
        val FIRST_INSTALL = booleanPreferencesKey("is_first_install")
        val ACTIVITY_RECOGNITION_RATIONALE_ALREADY_SHOWED = booleanPreferencesKey("act_rec_rationale_already_showed")
        val BACKGROUND_PERMISSION_REQUIRED = booleanPreferencesKey("background_permission_required")
        val IS_STEP_TRACKING_PAUSED = booleanPreferencesKey("is_step_tracking_paused")
    }

    override val isFirstInstall: Flow<Boolean>
        get() = dataStore.data.map { it[Keys.FIRST_INSTALL] ?: true }

    override val activityRecognitionRationaleShowed: Flow<Boolean>
        get() = dataStore.data.map { it[Keys.ACTIVITY_RECOGNITION_RATIONALE_ALREADY_SHOWED] ?: false }

    override val backgroundPermissionRequired: Flow<Boolean>
        get() = dataStore.data.map { it[Keys.BACKGROUND_PERMISSION_REQUIRED] ?: false }

    override suspend fun markFirstInstallCompleted() {
        dataStore.edit { it[Keys.FIRST_INSTALL] = false }
    }

    override suspend fun markActivityRecognitionRationaleShowed() {
        dataStore.edit { it[Keys.ACTIVITY_RECOGNITION_RATIONALE_ALREADY_SHOWED] = true }
    }

    override suspend fun markBackgroundPermissionRequired() {
        dataStore.edit { it[Keys.BACKGROUND_PERMISSION_REQUIRED] = true }
    }

    override val isStepTrackingPaused: Flow<Boolean>
        get() = dataStore.data.map { it[Keys.IS_STEP_TRACKING_PAUSED] ?: false }

    override suspend fun setStepTrackingPaused(isPaused: Boolean) {
        dataStore.edit { it[Keys.IS_STEP_TRACKING_PAUSED] = isPaused }
    }
}