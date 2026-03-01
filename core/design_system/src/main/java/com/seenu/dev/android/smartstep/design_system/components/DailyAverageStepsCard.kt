package com.seenu.dev.android.smartstep.design_system.components

import android.icu.text.NumberFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.annotations.MultiPreview
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.additionalGreen
import com.seenu.dev.android.smartstep.design_system.theme.backgroundWhite
import com.seenu.dev.android.smartstep.design_system.theme.bodyMediumMedium
import com.seenu.dev.android.smartstep.design_system.theme.buttonSecondary
import com.seenu.dev.android.smartstep.design_system.theme.textWhite

@MultiPreview
@Composable
private fun DailyAverageStepsCard_Preview() {
    SmartStepTheme {
        val data = DailyAverageStepsCardData(
            averageStepsPerDay = 5000,
            targetPerDay = 10000,
            stepsPerDay = mapOf(
                Day.SUNDAY to 3000,
                Day.MONDAY to 4000,
                Day.TUESDAY to 5000,
                Day.WEDNESDAY to 6000,
                Day.THURSDAY to 7000,
                Day.FRIDAY to 8000,
                Day.SATURDAY to 900000
            )
        )
        DailyAverageStepsCard(
            data = data,
            modifier = Modifier
        )
    }
}

@Composable
fun DailyAverageStepsCard(data: DailyAverageStepsCardData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val numberFormatter = remember {
            NumberFormat.getInstance()
        }
        Text(
            text = stringResource(
                R.string.daily_average_steps,
                numberFormatter.format(data.averageStepsPerDay)
            ),
            color = MaterialTheme.colorScheme.buttonSecondary,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (day in Day.entries) {
                val steps = data.stepsPerDay[day] ?: 0
                StepsPerDay(
                    dayText = day.getString(),
                    steps = steps,
                    target = data.targetPerDay,
                    stepCountLabel = numberFormatter.format(steps),
                    modifier = Modifier.weight(1F)
                )
            }
        }
    }
}

@Stable
data class DailyAverageStepsCardData constructor(
    val averageStepsPerDay: Int,
    val targetPerDay: Int,
    val stepsPerDay: Map<Day, Int> = mapOf()
)

enum class Day {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY
}

@Composable
fun Day.getString(): String {
    val res = when (this) {
        Day.SUNDAY -> R.string.day_sunday
        Day.MONDAY -> R.string.day_monday
        Day.TUESDAY -> R.string.day_tuesday
        Day.WEDNESDAY -> R.string.day_wednesday
        Day.THURSDAY -> R.string.day_thursday
        Day.FRIDAY -> R.string.day_friday
        Day.SATURDAY -> R.string.day_saturday
    }
    return stringResource(res)
}

@Preview
@Composable
private fun StepsPerDay_Preview() {
    SmartStepTheme {
        StepsPerDay(
            dayText = "Sun",
            steps = 100,
            target = 1000,
            stepCountLabel = "100",
            modifier = Modifier.width(80.dp)
        )
    }
}

@Composable
fun StepsPerDay(
    dayText: String,
    steps: Int,
    target: Int,
    stepCountLabel: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val circleColor = MaterialTheme.colorScheme.backgroundWhite
        val progressColor = MaterialTheme.colorScheme.additionalGreen
        Canvas(modifier = Modifier.aspectRatio(1F)) {
            val strokeWidth = 4.dp.toPx()
            val topLeft = Offset(
                strokeWidth / 2,
                strokeWidth / 2
            )
            val size = Size(
                size.width - strokeWidth,
                size.height - strokeWidth
            )

            drawArc(
                color = circleColor,
                startAngle = 0F,
                sweepAngle = 360F,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            val startAngle = -90F
            val endAngle = ((steps / target.toFloat()) * 360F)
            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = endAngle,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = dayText,
            color = MaterialTheme.colorScheme.textWhite,
            style = MaterialTheme.typography.bodyMediumMedium
        )

        Text(
            text = stepCountLabel,
            color = MaterialTheme.colorScheme.buttonSecondary,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}