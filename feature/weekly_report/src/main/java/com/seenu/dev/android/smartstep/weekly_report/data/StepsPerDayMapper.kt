package com.seenu.dev.android.smartstep.weekly_report.data

import com.seenu.dev.android.smartstep.home.home_domain.model.StepsPerDay
import com.seenu.dev.android.smartstep.weekly_report.domain.model.DayReport

fun StepsPerDay.toDayReport(): DayReport {
    return DayReport(
        date = date,
        steps = steps,
        goal = goal
    )
}