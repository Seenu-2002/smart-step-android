package com.seenu.dev.android.smartstep.home.home_domain

import kotlinx.coroutines.flow.Flow

interface StepSensorPreferences {

    val todayDate: Flow<String?>

    val todayStepsOffset: Flow<Int?>

    suspend fun updateTodayData(today: String, todayStepsOffset: Int)

}