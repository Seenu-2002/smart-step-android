package com.seenu.dev.android.smartstep.ai_coach.data

import java.util.Calendar

object TimeOfDayProvider {

    fun getTimeOfDay(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "morning"
            hour < 18 -> "day"
            else -> "evening"
        }
    }
}
