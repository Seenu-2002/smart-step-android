package com.seenu.dev.android.smartstep.weekly_report.domain.`interface`

import com.seenu.dev.android.smartstep.weekly_report.domain.model.DayReport
import com.seenu.dev.android.smartstep.weekly_report.domain.model.Metrics
import java.time.LocalDate

interface WeeklyReportRepository {

    suspend fun getWeeklyReport(startDate: LocalDate, endDate: LocalDate, metric: Metrics): List<DayReport>
}