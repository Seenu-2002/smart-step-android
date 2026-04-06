package com.seenu.dev.android.smartstep.weekly_report.domain.usecase

import com.seenu.dev.android.smartstep.weekly_report.domain.`interface`.WeeklyReportRepository
import com.seenu.dev.android.smartstep.weekly_report.domain.model.DayReport
import com.seenu.dev.android.smartstep.weekly_report.domain.model.Metrics
import com.seenu.dev.android.smartstep.weekly_report.domain.model.WeeklyReport
import java.time.LocalDate

class GetWeeklyReportUseCase(
    val weeklyReportRepository: WeeklyReportRepository
) {

    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate, metric: Metrics): WeeklyReport {
        val dailyReports = when (metric) {
            Metrics.STEPS -> weeklyReportRepository.getWeeklyReport(startDate, endDate, Metrics.STEPS)
        }
        val fullRangeDailyReports = fillMissingSteps(
            dailyReports,
            startDate,
            endDate
        )

        val totalSteps = fullRangeDailyReports.sumOf { it.steps }
        val averageSteps = fullRangeDailyReports.filter{ it.steps > 0 }.map { it.steps }.average()

        return WeeklyReport(
            totalValue = totalSteps.toDouble(),
            averageValue = averageSteps,
            dailyReports = fullRangeDailyReports
        )
    }
}

private fun fillMissingSteps(
    input: List<DayReport>,
    start: LocalDate,
    end: LocalDate
): List<DayReport> {

    val map = input.associateBy { it.date }

    return generateSequence(start) { current ->
        if (current < end) current.plusDays(1) else null
    }.map { date ->
        val dateStr = date.toString() // yyyy-MM-dd

        map[dateStr] ?: DayReport(
            date = dateStr,
            steps = 0,
            goal = null
        )
    }.toList()
}