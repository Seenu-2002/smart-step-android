@file:OptIn(FlowPreview::class)

package com.seenu.dev.android.smartstep.home.home_data

import android.util.Log
import com.seenu.dev.android.smartstep.home.home_data.datasource.StepSensorDataSource
import com.seenu.dev.android.smartstep.home.home_data.local.database.DailyStepsDao
import com.seenu.dev.android.smartstep.home.home_data.local.database.entities.DailyStepsEntity
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorPreferences
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class StepSensorRepositoryImpl(
    private val stepSensorDataSource: StepSensorDataSource,
    private val stepsDao: DailyStepsDao,
    private val stepSensorPreferences: StepSensorPreferences
): StepSensorRepository {
    override suspend fun startCountingSteps() {
        Log.d("asd", "startCountingSteps: ")
        stepSensorDataSource.start()

        var savedDate = stepSensorPreferences.todayDate.first()
        var offset = stepSensorPreferences.todayStepsOffset.first() ?: 0

        stepSensorDataSource.steps
            .sample(5_000L)
            .collect { totalSensorSteps ->

                Log.d("asd", "startCountingSteps: collecting in repo")
                val today = getToday()

                if (today != savedDate) {
                    savedDate = today
                    offset = totalSensorSteps

                    stepSensorPreferences.updateTodayData(today, offset)
                }

                val todaySteps = totalSensorSteps - offset

                stepsDao.upsertDailySteps(
                    DailyStepsEntity(
                        date = today,
                        steps = todaySteps
                    )
                )
            }
    }

    override fun stopCountingSteps() {
        stepSensorDataSource.stop()
    }

    override fun observeTodaySteps(): Flow<Int> {
        return stepsDao
            .getDailySteps(getToday())
            .map { it?.steps ?: 0 }
    }
}

fun getToday(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}