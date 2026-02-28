package com.seenu.dev.android.smartstep.profile_setup.mapper

import com.seenu.dev.android.smartstep.design_system.components.HeightMetricUiModel
import com.seenu.dev.android.smartstep.design_system.components.WeightMetricUiModel
import com.seenu.dev.android.smartstep.domain.model.HeightMetric
import com.seenu.dev.android.smartstep.domain.model.WeightMetric

fun HeightMetric.toUiModel() = when (this) {
    is HeightMetric.Centimeters -> HeightMetricUiModel.Centimeters(value = value)
    is HeightMetric.FeetInches -> HeightMetricUiModel.FeetInches(feet = feet, inches = inches)
}

fun WeightMetric.toUiModel() = when (this) {
    is WeightMetric.Kilograms -> WeightMetricUiModel.Kilograms(
        weight = weight
    )

    is WeightMetric.Pounds -> WeightMetricUiModel.Pounds(
        weight = weight
    )
}

fun HeightMetricUiModel.toDomainModel() = when (this) {
    is HeightMetricUiModel.Centimeters -> HeightMetric.Centimeters(value = value)
    is HeightMetricUiModel.FeetInches -> HeightMetric.FeetInches(feet = feet, inches = inches)
}

fun WeightMetricUiModel.toDomainModel() = when (this) {
    is WeightMetricUiModel.Kilograms -> WeightMetric.Kilograms(
        weight = weight
    )

    is WeightMetricUiModel.Pounds -> WeightMetric.Pounds(
        weight = weight
    )
}