package com.seenu.dev.android.smartstep.home.home_data

import com.seenu.dev.android.smartstep.home.home_data.local.StepDao
import com.seenu.dev.android.smartstep.home.home_data.local.StepEntity
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StepRepositoryImpl(
    private val stepDao: StepDao
) : StepRepository {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // todo better use LocalDate.now().toString() but need to add desugar in gradle
    private val today: String
        get() = dateFormatter.format(Calendar.getInstance().time)

    override fun getTodaySteps(): Flow<Int> {
        return stepDao.observeStepsForDate(today).map { it?.stepCount ?: 0 }
    }

    override fun getTodayActiveSeconds(): Flow<Long> {
        return stepDao.observeStepsForDate(today).map { it?.activeSeconds ?: 0L }
    }

    override suspend fun addStep(activeTimeDeltaSeconds: Long) {
        val currentDate = today
        val currentEntry = stepDao.getStepsForDate(currentDate)

        if (currentEntry == null) {
            stepDao.upsertStepData(
                StepEntity(currentDate, 1, activeTimeDeltaSeconds)
            )
        } else {
            stepDao.upsertStepData(
                currentEntry.copy(
                    stepCount = currentEntry.stepCount + 1,
                    activeSeconds = currentEntry.activeSeconds + activeTimeDeltaSeconds
                )
            )
        }
    }

    override suspend fun updateStepsManually(newStepCount: Int) {
        val currentDate = today
        val currentEntry = stepDao.getStepsForDate(currentDate)

        if (currentEntry == null) {
            stepDao.upsertStepData(StepEntity(currentDate, newStepCount, 0L))
        } else {
            stepDao.upsertStepData(currentEntry.copy(stepCount = newStepCount))
        }
    }

    override suspend fun resetToday() {
        stepDao.deleteDate(today)
    }
}