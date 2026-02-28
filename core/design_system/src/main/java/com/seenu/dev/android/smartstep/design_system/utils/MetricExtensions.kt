package com.seenu.dev.android.smartstep.design_system.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.components.HeightMetricUiModel
import com.seenu.dev.android.smartstep.design_system.components.WeightMetricUiModel

@Composable
fun HeightMetricUiModel.getString(): String {
    return when (this) {
        is HeightMetricUiModel.Centimeters -> {
            stringResource(
                R.string.height_cm,
                value
            )
        }

        is HeightMetricUiModel.FeetInches -> {
            stringResource(
                R.string.height_feet_inches,
                feet,
                inches
            )
        }
    }
}

@Composable
fun WeightMetricUiModel.getString(): String {
    return when (this) {
        is WeightMetricUiModel.Kilograms -> {
            stringResource(
                R.string.weight_kg,
                this.weight
            )
        }

        is WeightMetricUiModel.Pounds -> {
            stringResource(
                R.string.weight_lbs,
                this.weight
            )
        }
    }
}

