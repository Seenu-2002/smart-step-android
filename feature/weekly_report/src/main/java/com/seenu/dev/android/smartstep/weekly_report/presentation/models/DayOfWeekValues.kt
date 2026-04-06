package com.seenu.dev.android.smartstep.weekly_report.presentation.models

data class DayOfWeekValues(
    val dayOfWeek: DayOfWeek,
    val value: Double,
    val goal: Double?,
    val isToday: Boolean
) {
    fun hasData(): Boolean {
        return value > 0
    }

    fun getStatus(): ReportStatus {
        return when {
            isToday -> ReportStatus.CURRENT
            hasData() -> ReportStatus.COMPLETED
            else -> ReportStatus.INACTIVE
        }
    }
}

enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

enum class ReportStatus {
    COMPLETED, INACTIVE, CURRENT
}
