package com.seenu.dev.android.smartstep.home.home_data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal val Context.stepSensorDataStore: DataStore<Preferences> by preferencesDataStore(name = "step_sensor_prefs")


class StepSensorPreferencesImpl(
    private val dataStore: DataStore<Preferences>
): StepSensorPreferences {

    private object Keys {
        val TODAY_DATE = stringPreferencesKey("today_date")
        val TODAY_STEPS_OFFSET = intPreferencesKey("today_steps_offset")
    }

    override val todayDate: Flow<String?>
        get() = dataStore.data.map { it[Keys.TODAY_DATE] }

    override val todayStepsOffset: Flow<Int?>
        get() = dataStore.data.map { it[Keys.TODAY_STEPS_OFFSET] }

    override suspend fun updateTodayData(today: String, todayStepsOffset: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.TODAY_DATE] = today
            preferences[Keys.TODAY_STEPS_OFFSET] = todayStepsOffset
        }
    }

}