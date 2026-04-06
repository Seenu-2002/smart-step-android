package com.seenu.dev.android.smartstep.weekly_report.presentation.utils

import com.seenu.dev.android.smartstep.weekly_report.presentation.models.WeekRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object WeekRangeUtils {

    /**
     * Returns the Monday-to-Sunday range for the week containing the [anchorDate].
     */
    fun getWeekRange(anchorDate: LocalDate): WeekRange {
        // Find the Monday of this week
        val monday = anchorDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        // Sunday is always 6 days after Monday
        val sunday = monday.plusDays(6)

        return WeekRange(monday, sunday, hasNextWeek = false)
    }

    /**
     * Helper to get the current week's range
     */
    fun getCurrentWeekRange(): WeekRange = getWeekRange(LocalDate.now())

    fun getPreviousWeekRange(currentWeekRange: WeekRange): WeekRange {
        val currentMonday = currentWeekRange.start
        val currentSunday = currentWeekRange.end
        val previousMonday = currentMonday.minusWeeks(1)
        val previousSunday = currentSunday.minusWeeks(1)
        return WeekRange(previousMonday, previousSunday, hasNextWeek = true)
    }

    fun getNextWeekRange(currentWeekRange: WeekRange): WeekRange? {
        if (!currentWeekRange.hasNextWeek) return null

        val currentMonday = currentWeekRange.start
        val currentSunday = currentWeekRange.end
        val nextMonday = currentMonday.plusWeeks(1)
        val nextSunday = currentSunday.plusWeeks(1)
        val todayInCalculatedWeekRange = LocalDate.now() in nextMonday..nextSunday
        return WeekRange(nextMonday, nextSunday, hasNextWeek = !todayInCalculatedWeekRange)
    }
}