package com.seenu.dev.android.smartstep.home.home_domain

object StepMetricsCalculator {

    private const val STRIDE_MULTIPLIER = 0.415f
    private const val KCAL_MULTIPLIER = 0.0005f
    private const val METERS_IN_KM = 1000f
    private const val METERS_IN_MILE = 1609.34f
    private const val LBS_TO_KG = 0.453592f

    fun calculateDistance(steps: Int, heightCm: Float, isMetricKm: Boolean): Float {
        val stepLengthCm = heightCm * STRIDE_MULTIPLIER
        val distanceMeters = (steps * stepLengthCm) / 100f

        return distanceMeters /
            if (isMetricKm) METERS_IN_KM
            else METERS_IN_MILE
    }

    fun calculateCalories(steps: Int, weight: Float, isWeightLbs: Boolean, isMale: Boolean): Int {
        val weightKg = if (isWeightLbs) weight * LBS_TO_KG else weight
        val genderFactor = if (isMale) 1.0f else 0.9f

        val kcalPerStep = weightKg * KCAL_MULTIPLIER * genderFactor
        return (steps * kcalPerStep).toInt()
    }

    fun calculateActiveMinutes(activeSeconds: Long): Int = (activeSeconds / 60).toInt()
}