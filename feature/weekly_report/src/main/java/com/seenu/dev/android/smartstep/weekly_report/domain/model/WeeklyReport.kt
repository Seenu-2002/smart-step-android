package com.seenu.dev.android.smartstep.weekly_report.domain.model

data class WeeklyReport(
    val totalValue: Double,
    val averageValue: Double,
    val dailyReports: List<DayReport>
)
