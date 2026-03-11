package com.seenu.dev.android.smartstep.home.home_domain

import kotlinx.coroutines.flow.Flow

interface StepSensorRepository {

    suspend fun startCountingSteps()

    fun stopCountingSteps()

    fun observeTodaySteps(): Flow<Int>

}