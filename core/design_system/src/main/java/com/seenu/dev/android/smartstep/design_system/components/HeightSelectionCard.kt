package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.seenu.dev.android.smartstep.design_system.theme.backgroundTertiary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.bodyMediumMedium
import com.seenu.dev.android.smartstep.design_system.theme.buttonSecondary
import kotlin.math.roundToInt

@Preview
@Composable
private fun HeightSelectionCard_PreviewCms() {
    SmartStepTheme {
        var selectedMetric: HeightMetricUiModel by remember {
            mutableStateOf(HeightMetricUiModel.Centimeters(175))
        }
        HeightSelectionCard(
            selectedMetric = selectedMetric,
            onMetricTypeChange = { selectedMetric = it },
            onMetricChange = { selectedMetric = it }
        )
    }
}

@Preview
@Composable
private fun HeightSelectionCard_PreviewFtIn() {
    SmartStepTheme {
        var selectedMetric: HeightMetricUiModel by remember {
            mutableStateOf(HeightMetricUiModel.FeetInches(5, 8))
        }
        HeightSelectionCard(
            selectedMetric = selectedMetric,
            onMetricTypeChange = { selectedMetric = it },
            onMetricChange = { selectedMetric = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeightSelectionCard(
    selectedMetric: HeightMetricUiModel,
    onMetricTypeChange: (HeightMetricUiModel) -> Unit,
    onMetricChange: (HeightMetricUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onOk: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
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
                    R.string.height,
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
                    selected = selectedMetric is HeightMetricUiModel.Centimeters, onClick = {
                        if (selectedMetric is HeightMetricUiModel.FeetInches) {
                            onMetricTypeChange(selectedMetric.toCentimeters())
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
                        text = stringResource(R.string.unit_cm),
                        style = MaterialTheme.typography.bodyMediumMedium
                    )
                }
                SegmentedButton(
                    selected = selectedMetric is HeightMetricUiModel.FeetInches, onClick = {
                        if (selectedMetric is HeightMetricUiModel.Centimeters) {
                            onMetricTypeChange(selectedMetric.toFeetInches())
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
                        text = stringResource(R.string.unit_ft_inches),
                        style = MaterialTheme.typography.bodyMediumMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            HeightMetricValueContainer(
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
sealed interface HeightMetricUiModel {
    data class Centimeters(val value: Int) : HeightMetricUiModel {
        fun toFeetInches(): FeetInches {
            val totalInches = (value / 2.54).roundToInt()
            val feet = totalInches / 12
            val inches = totalInches % 12
            return FeetInches(feet, inches)
        }
    }

    data class FeetInches(val feet: Int, val inches: Int) : HeightMetricUiModel {
        fun toCentimeters(): Centimeters {
            val totalInches = (feet * 12) + inches
            val centimeters = (totalInches * 2.54).roundToInt()
            return Centimeters(centimeters)
        }
    }
}

@Composable
fun HeightMetricValueContainer(
    metric: HeightMetricUiModel,
    onMetricChange: (HeightMetricUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    if (metric is HeightMetricUiModel.Centimeters) {
        ScrollableHapticContainer(
            options = (140..328).map { it.toString() },
            visibleCount = 5,
            activeIndex = metric.value - 140,
            onActiveIndexChange = {
                onMetricChange(HeightMetricUiModel.Centimeters(value = it + 140))
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
    } else if (metric is HeightMetricUiModel.FeetInches) {
        Row(modifier = modifier) {
            ScrollableHapticContainer(
                options = (1..10).map { it.toString() },
                visibleCount = 5,
                activeIndex = metric.feet - 1,
                onActiveIndexChange = {
                    onMetricChange(
                        metric.copy(
                            feet = it + 1
                        )
                    )
                },
                itemHeight = 40.dp,
                modifier = Modifier.weight(1F),
                content = { text, isSelected ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                    )
                },
                selectedBox = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 40.dp)
                            .background(color = MaterialTheme.colorScheme.backgroundTertiary)
                            .padding(end = 12.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = stringResource(R.string.unit_ft),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
            ScrollableHapticContainer(
                options = (0..9).map { it.toString() },
                visibleCount = 5,
                activeIndex = metric.inches,
                onActiveIndexChange = {
                    onMetricChange(
                        metric.copy(
                            inches = it
                        )
                    )
                },
                itemHeight = 40.dp,
                modifier = Modifier.weight(1F),
                content = { text, isSelected ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                    )
                },
                selectedBox = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 40.dp)
                            .background(color = MaterialTheme.colorScheme.backgroundTertiary)
                            .padding(end = 24.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = stringResource(R.string.unit_inches),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    }
}