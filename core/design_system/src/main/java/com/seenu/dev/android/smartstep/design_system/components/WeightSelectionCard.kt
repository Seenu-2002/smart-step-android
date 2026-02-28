package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.bodyMediumMedium
import com.seenu.dev.android.smartstep.design_system.theme.buttonSecondary
import kotlin.math.roundToInt

@Preview
@Composable
private fun WeightSelectionCard_PreviewKgs() {
    SmartStepTheme {
        var selectedMetric by remember {
            mutableStateOf<WeightMetricUiModel>(WeightMetricUiModel.Kilograms(70))
        }
        WeightSelectionCard(
            selectedMetric = selectedMetric,
            onMetricTypeChange = {
                selectedMetric = it
            },
            onMetricChange = {
                selectedMetric = it
            },
        )
    }
}

@Preview
@Composable
private fun WeightSelectionCard_PreviewLbs() {
    SmartStepTheme {
        var selectedMetric by remember {
            mutableStateOf<WeightMetricUiModel>(WeightMetricUiModel.Pounds(170))
        }
        WeightSelectionCard(
            selectedMetric = selectedMetric,
            onMetricTypeChange = {
                selectedMetric = it
            },
            onMetricChange = {
                selectedMetric = it
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightSelectionCard(
    selectedMetric: WeightMetricUiModel,
    onMetricTypeChange: (WeightMetricUiModel) -> Unit,
    onMetricChange: (WeightMetricUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onOk: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.backgroundSecondary,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Text(
                text = stringResource(
                    R.string.weight,
                ),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.usage_msg),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                SegmentedButton(
                    selected = selectedMetric is WeightMetricUiModel.Kilograms, onClick = {
                        if (selectedMetric is WeightMetricUiModel.Pounds) {
                            onMetricTypeChange(selectedMetric.toKilograms())
                        }
                    }, colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.buttonSecondary,
                        activeBorderColor = MaterialTheme.colorScheme.buttonSecondary,
                        inactiveContainerColor = Color.Transparent,
                        inactiveBorderColor = MaterialTheme.colorScheme.outline,
                    ), shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = 2
                    )
                ) {
                    Text(
                        text = stringResource(R.string.unit_kg),
                        style = MaterialTheme.typography.bodyMediumMedium
                    )
                }
                SegmentedButton(
                    selected = selectedMetric is WeightMetricUiModel.Pounds, onClick = {
                        if (selectedMetric is WeightMetricUiModel.Kilograms) {
                            onMetricTypeChange(selectedMetric.toPounds())
                        }
                    }, colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.buttonSecondary,
                        activeBorderColor = MaterialTheme.colorScheme.buttonSecondary,
                        inactiveContainerColor = Color.Transparent,
                        inactiveBorderColor = MaterialTheme.colorScheme.outline,
                    ), shape = SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = 2
                    )
                ) {
                    Text(
                        text = stringResource(R.string.unit_lbs),
                        style = MaterialTheme.typography.bodyMediumMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            WeightMetricValueContainer(
                metric = selectedMetric,
                onMetricChange = onMetricChange,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = onDismissRequest)
                        .padding(
                            10.dp
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.ok),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = onOk)
                        .padding(
                            10.dp
                        )
                )
            }
        }
    }
}

@Stable
sealed interface WeightMetricUiModel {
    data class Kilograms(val weight: Int) : WeightMetricUiModel {
        fun toPounds(): Pounds {
            return Pounds((weight * 2.20462).roundToInt())
        }
    }

    data class Pounds(val weight: Int) : WeightMetricUiModel {
        fun toKilograms(): Kilograms {
            return Kilograms((weight / 2.20462).roundToInt())
        }
    }
}

@Composable
fun WeightMetricValueContainer(
    metric: WeightMetricUiModel,
    onMetricChange: (WeightMetricUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = remember(metric) {
        if (metric is WeightMetricUiModel.Kilograms) {
            (30..200)
        } else {
            (66..440)
        }
    }.map { it.toString() }
    val activeIndex = remember(metric) {
        options.indexOf(
            when (metric) {
                is WeightMetricUiModel.Kilograms -> metric.weight.toString()
                is WeightMetricUiModel.Pounds -> metric.weight.toString()
            }
        )
    }
    ScrollableHapticContainer(
        options = options,
        visibleCount = 5,
        activeIndex = activeIndex,
        onActiveIndexChange = {
            val weight = options[it].toIntOrNull() ?: return@ScrollableHapticContainer
            when (metric) {
                is WeightMetricUiModel.Kilograms -> onMetricChange(WeightMetricUiModel.Kilograms(weight))
                is WeightMetricUiModel.Pounds -> onMetricChange(WeightMetricUiModel.Pounds(weight))
            }
        },
        itemHeight = 40.dp,
        modifier = modifier,
        content = { text, isSelected ->
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
            )
        }
    )
}