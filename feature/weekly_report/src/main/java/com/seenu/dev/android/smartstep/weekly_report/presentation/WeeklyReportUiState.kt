package com.seenu.dev.android.smartstep.weekly_report.presentation

import com.seenu.dev.android.smartstep.weekly_report.presentation.models.DayOfWeekValues
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.ReportMetric
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.WeekRange
import com.seenu.dev.android.smartstep.weekly_report.presentation.utils.WeekRangeUtils.getCurrentWeekRange

data class WeeklyReportUiState(
    val selectedMetric: ReportMetric = ReportMetric.Steps,
    val totalWeeklyValue: Double = 0.0,
    val dailyAverageValue: Double = 0.0,
    val weekRange: WeekRange = getCurrentWeekRange(),
    val weekValues: List<DayOfWeekValues> = emptyList(),
    val isLoading: Boolean = false
)
