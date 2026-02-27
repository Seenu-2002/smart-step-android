package com.seenu.dev.android.smartstep.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.seenu.dev.android.smartstep.domain.model.Gender
import com.seenu.dev.android.smartstep.domain.model.HeightMetric
import com.seenu.dev.android.smartstep.domain.model.UserConfig
import com.seenu.dev.android.smartstep.domain.model.WeightMetric
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.json.Json

class UserConfigRepositoryImpl constructor(
    private val context: Context
) : UserConfigRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_config_prefs")

    private val genderKey = stringPreferencesKey("gender")
    private val heightMetricKey = stringPreferencesKey("height_metric")
    private val weightMetricKey = stringPreferencesKey("weight_metric")
    private val isFirstSetupCompletedKey = booleanPreferencesKey("is_first_setup_completed")

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserConfigFlow(): Flow<UserConfig> {
        return context.dataStore.data.mapLatest { store ->
            store.toUserConfig()
        }
    }

    override suspend fun getUserConfig(): UserConfig {
        return context.dataStore.data.first().toUserConfig()
    }

    private fun Preferences.toUserConfig(): UserConfig {
        val heightMetric = this[heightMetricKey]?.let {
            Json.decodeFromString<HeightMetric>(it)
        } ?: HeightMetric.Centimeters(170)
        val weightMetric = this[weightMetricKey]?.let {
            Json.decodeFromString<WeightMetric>(it)
        } ?: WeightMetric.Kilograms(60)
        val gender = this[genderKey]?.let {
            Gender.valueOf(it.uppercase())
        } ?: Gender.FEMALE
        val isFirstSetupCompleted = this[isFirstSetupCompletedKey] ?: false

        return UserConfig(
            gender = gender,
            isFirstSetupCompleted = isFirstSetupCompleted,
            heightMetric = heightMetric,
            weightMetric = weightMetric
        )
    }

    override suspend fun updateUserConfig(userConfig: UserConfig) {
        updateHeightMetric(userConfig.heightMetric)
        updateWeightMetric(userConfig.weightMetric)
        updateGender(userConfig.gender)
        onFirstSetupCompleted()
    }

    override suspend fun updateHeightMetric(heightMetric: HeightMetric) {
        context.dataStore.edit { store ->
            store[heightMetricKey] = Json.encodeToString(heightMetric)
        }
    }

    override suspend fun updateWeightMetric(weightMetric: WeightMetric) {
        context.dataStore.edit { store ->
            store[weightMetricKey] = Json.encodeToString(weightMetric)
        }
    }

    override suspend fun updateGender(gender: Gender) {
        context.dataStore.edit { store ->
            store[genderKey] = gender.toString()
        }
    }

    override suspend fun onFirstSetupCompleted() {
        context.dataStore.edit { store ->
            store[isFirstSetupCompletedKey] = true
        }
    }

    override suspend fun onLogout() {
        context.dataStore.edit { store ->
            store.clear()
        }
    }


}