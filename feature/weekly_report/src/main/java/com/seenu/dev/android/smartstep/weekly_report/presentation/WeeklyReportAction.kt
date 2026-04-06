package com.seenu.dev.android.smartstep.weekly_report.presentation

sealed interface WeeklyReportAction {
    object OnNextWeekClicked: WeeklyReportAction
    object OnPreviousWeekClicked: WeeklyReportAction
}