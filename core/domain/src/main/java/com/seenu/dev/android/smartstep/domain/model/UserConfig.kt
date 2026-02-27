package com.seenu.dev.android.smartstep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserConfig constructor(
    val gender: Gender,
    val isFirstSetupCompleted: Boolean,
    val heightMetric: HeightMetric,
    val weightMetric: WeightMetric
)

@Serializable
enum class Gender {
    FEMALE, MALE
}

@Serializable
sealed interface HeightMetric {
    @Serializable
    data class Centimeters(val value: Int) : HeightMetric

    @Serializable
    data class FeetInches(val feet: Int, val inches: Int) : HeightMetric
}

@Serializable
sealed interface WeightMetric {
    @Serializable
    data class Kilograms(val weight: Int) : WeightMetric

    @Serializable
    data class Pounds(val weight: Int) : WeightMetric
}