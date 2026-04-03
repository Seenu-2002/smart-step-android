package com.seenu.dev.android.smartstep.weekly_report.presentation.models

import java.time.LocalDate

data class WeekRange(
    val start: LocalDate,
    val end: LocalDate,
    val hasNextWeek: Boolean
)