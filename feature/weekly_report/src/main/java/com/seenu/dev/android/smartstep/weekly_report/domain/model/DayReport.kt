package com.seenu.dev.android.smartstep.weekly_report.domain.model

data class DayReport(
    val date: String,
    val steps: Int,
    val goal: Int?
)
