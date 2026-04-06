package com.seenu.dev.android.smartstep.weekly_report.data

import android.util.Log
import com.seenu.dev.android.smartstep.home.home_data.local.StepDao
import com.seenu.dev.android.smartstep.home.home_domain.model.StepsPerDay
import com.seenu.dev.android.smartstep.weekly_report.domain.`interface`.WeeklyReportRepository
import com.seenu.dev.android.smartstep.weekly_report.domain.model.DayReport
import com.seenu.dev.android.smartstep.weekly_report.domain.model.Metrics
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class WeeklyReportRepositoryImpl(
    private val stepsDao: StepDao
): WeeklyReportRepository {
    override suspend fun getWeeklyReport(
        startDate: LocalDate,
        endDate: LocalDate,
        metric: Metrics
    ): List<DayReport> {
        val startDateString = DateMapper.localDateToString(startDate)
        val endDateString = DateMapper.localDateToString(endDate)

        return stepsDao.getStepsForDateRangeFlow(startDateString, endDateString)
            .first()
            .map { it.toDayReport() }
    }
}
