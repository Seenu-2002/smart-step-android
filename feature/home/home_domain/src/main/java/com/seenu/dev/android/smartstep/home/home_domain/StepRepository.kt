package com.seenu.dev.android.smartstep.home.home_domain

import com.seenu.dev.android.smartstep.home.home_domain.model.StepsPerDay
import kotlinx.coroutines.flow.Flow

interface StepRepository {
    // Flows for the ViewModel to observe
    fun getTodaySteps(): Flow<Int>
    fun getTodayActiveSeconds(): Flow<Long>
    
    // Actions for the Sensor and UI
    suspend fun updateStepsManually(newStepCount: Int, date: String)
    suspend fun updateStepGoal(newStepGoal: Int, date: String)
    suspend fun resetToday()
    suspend fun startCountingSteps()
    fun stopCountingSteps()
    fun getStepsForRangeFlow(startDate: String, endDate: String): Flow<List<StepsPerDay>>
}