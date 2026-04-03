package com.seenu.dev.android.smartstep.weekly_report.presentation.models

import androidx.annotation.StringRes
import com.seenu.dev.android.smartstep.weekly_report.R

sealed class ReportMetric(
    @param:StringRes val nameRes: Int,
    @param:StringRes val unitRes: Int
) {

    data object Steps: ReportMetric(
        nameRes = R.string.steps,
        unitRes = R.string.steps_unit,
    )

    data object Calories: ReportMetric(
        nameRes = R.string.calories,
        unitRes = R.string.calories_unit,
    )

    data object Time: ReportMetric(
        nameRes = R.string.time,
        unitRes = R.string.time_unit,
    )

    data object Distance: ReportMetric(
        nameRes = R.string.distance,
        unitRes = R.string.distance_unit,
    )
}