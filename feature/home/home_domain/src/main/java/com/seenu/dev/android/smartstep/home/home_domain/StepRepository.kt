package com.seenu.dev.android.smartstep.home.home_domain

import kotlinx.coroutines.flow.Flow

interface StepRepository {
    // Flows for the ViewModel to observe
    fun getTodaySteps(): Flow<Int>
    fun getTodayActiveSeconds(): Flow<Long>
    
    // Actions for the Sensor and UI
    suspend fun addStep(activeTimeDeltaSeconds: Long)
    suspend fun updateStepsManually(newStepCount: Int)
    suspend fun resetToday()
}