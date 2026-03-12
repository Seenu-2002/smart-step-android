package com.seenu.dev.android.smartstep.domain.extensions

import com.seenu.dev.android.smartstep.domain.model.HeightMetric
import kotlin.math.roundToInt

fun HeightMetric.FeetInches.toCentimeters(): Int {
    val totalInches = (feet * 12) + inches
    val centimeters = (totalInches * 2.54).roundToInt()
    return centimeters
}