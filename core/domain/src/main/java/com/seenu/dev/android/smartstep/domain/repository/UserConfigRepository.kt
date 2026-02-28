package com.seenu.dev.android.smartstep.domain.repository

import com.seenu.dev.android.smartstep.domain.model.Gender
import com.seenu.dev.android.smartstep.domain.model.HeightMetric
import com.seenu.dev.android.smartstep.domain.model.UserConfig
import com.seenu.dev.android.smartstep.domain.model.WeightMetric
import kotlinx.coroutines.flow.Flow

interface UserConfigRepository {

    fun getUserConfigFlow(): Flow<UserConfig>

    suspend fun getUserConfig(): UserConfig

    suspend fun updateUserConfig(userConfig: UserConfig)

    suspend fun updateHeightMetric(heightMetric: HeightMetric)

    suspend fun updateWeightMetric(weightMetric: WeightMetric)

    suspend fun updateGender(gender: Gender)

    suspend fun onFirstSetupCompleted()

    suspend fun onLogout()

}