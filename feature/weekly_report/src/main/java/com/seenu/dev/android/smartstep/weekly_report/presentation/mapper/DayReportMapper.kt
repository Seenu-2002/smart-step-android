package com.seenu.dev.android.smartstep.weekly_report.presentation.mapper

import com.seenu.dev.android.smartstep.weekly_report.domain.model.DayReport
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.DayOfWeek
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.DayOfWeekValues
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun List<DayReport>.toDayOfWeekValues(): List<DayOfWeekValues> {
    return mapIndexed { index, dayReport ->
        DayOfWeekValues(
            dayOfWeek = getDayOfWeekByIndex(index),
            value = dayReport.steps.toDouble(),
            goal = dayReport.goal?.toDouble(),
            isToday = isToday(dayReport.date)
        )
    }
}

private fun getDayOfWeekByIndex(index: Int): DayOfWeek {
    return when (index) {
        0 -> DayOfWeek.MONDAY
        1 -> DayOfWeek.TUESDAY
        2 -> DayOfWeek.WEDNESDAY
        3 -> DayOfWeek.THURSDAY
        4 -> DayOfWeek.FRIDAY
        5 -> DayOfWeek.SATURDAY
        else -> DayOfWeek.SUNDAY
    }
}

private fun isToday(dateString: String): Boolean {
    return try {
        // 1. Parse the string (expects yyyy-MM-dd by default)
        val parsedDate = LocalDate.parse(dateString)

        // 2. Compare with today's date
        val today = LocalDate.now()

        parsedDate == today
    } catch (e: DateTimeParseException) {
        // Return false if the string format is invalid
        false
    }
}