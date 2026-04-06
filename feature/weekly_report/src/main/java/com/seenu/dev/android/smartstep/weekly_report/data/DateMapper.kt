package com.seenu.dev.android.smartstep.weekly_report.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateMapper {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun localDateToString(date: LocalDate): String = formatter.format(date)
}