@file:OptIn(FlowPreview::class)

package com.seenu.dev.android.smartstep.home.home_data

import com.seenu.dev.android.smartstep.home.home_data.local.DailyStepEntity
import com.seenu.dev.android.smartstep.home.home_data.local.StepDao
import com.seenu.dev.android.smartstep.home.home_data.sensor.StepSensorDataSource
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorPreferences
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StepRepositoryImpl(
    private val stepSensorDataSource: StepSensorDataSource,
    private val stepDao: StepDao,
    private val stepSensorPreferences: StepSensorPreferences
) : StepRepository {

    override fun getTodaySteps(): Flow<Int> {
        return stepDao.getStepsForDateFlow(getToday()).map { it?.stepCount ?: 0 }
    }

    override fun getTodayActiveSeconds(): Flow<Long> {
        return stepDao.getStepsForDateFlow(getToday()).map { it?.activeSeconds ?: 0L }
    }

    override suspend fun updateStepsManually(newStepCount: Int) {
        val currentDate = getToday()
        val currentEntry = stepDao.getStepsForDateSync(currentDate)

        if (currentEntry == null) {
            stepDao.upsertStepData(DailyStepEntity(currentDate, newStepCount, 0L))
        } else {
            stepDao.upsertStepData(currentEntry.copy(stepCount = newStepCount))
        }
    }

    override suspend fun resetToday() {
        stepDao.deleteDate(getToday())
    }

    override suspend fun startCountingSteps() {
        stepSensorDataSource.startListening()

        var savedDate = stepSensorPreferences.todayDate.first()
        var offset = stepSensorPreferences.todayStepsOffset.first() ?: 0

        stepSensorDataSource.steps
            .sample(5_000L)
            .collect { sensorData ->
                val today = getToday()

                if (today != savedDate) {
                    savedDate = today
                    offset = sensorData.totalSteps

                    stepSensorPreferences.updateTodayData(today, offset)
                }

                val todaySteps = sensorData.totalSteps - offset

                stepDao.upsertStepData(
                    DailyStepEntity(
                        date = today,
                        stepCount = todaySteps,
                        activeSeconds = sensorData.activeSeconds
                    )
                )
            }
    }

    override fun stopCountingSteps() {
        stepSensorDataSource.stopListening()
    }


}

fun getToday(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}