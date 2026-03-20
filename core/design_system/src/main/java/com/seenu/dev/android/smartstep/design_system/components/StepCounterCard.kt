package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.theme.Inter
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.textWhite
import com.seenu.dev.android.smartstep.design_system.theme.title

@Composable
fun StepCounterCard(
    currentStepCount: Int,
    targetStepCount: Int,

    isPaused: Boolean = false,
    isMetricKm: Boolean = true,

    distanceText: String = "0.0",
    caloriesText: String = "0",
    walkingTimeMinutesText: String = "0",

    onPenIconClick: (() -> Unit)? = null,
    onPausePlayIconClick: (() -> Unit)? = null,
) {
    Column(
        Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.textWhite) {

            Row {
                SmartCounterIcon(
                    painter = R.drawable.ic_sneakers,
                    contentDescription = "Sneaker Icon"
                )

                Spacer(modifier = Modifier.weight(1f))

                SmartCounterIcon(
                    painter = R.drawable.ic_pen,
                    shape = CircleShape,
                    contentDescription = "Sneaker Icon",
                    onClick = onPenIconClick
                )

                Spacer(modifier = Modifier.width(8.dp))

                SmartCounterIcon(
                    painter =
                        if (isPaused) R.drawable.ic_play
                        else R.drawable.ic_pause,
                    shape = CircleShape,
                    onClick = onPausePlayIconClick
                )
            }

            Text(
                text = stringResource(R.string.current_steps, currentStepCount),
                style = MaterialTheme.typography.title,
                fontFamily = Inter,
                color = MaterialTheme.colorScheme.textWhite.copy(
                    alpha = if (isPaused) 0.2f else 1f
                )
            )

            Text(
                text =
                    if (isPaused) stringResource(R.string.paused)
                    else stringResource(
                        R.string.target_steps,
                        targetStepCount
                    ),
                style = MaterialTheme.typography.bodyLargeMedium
            )

            LinearProgressIndicator(
                progress = {
                    (currentStepCount.toFloat() / targetStepCount.toFloat()).coerceIn(0f, 1f)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        MaterialTheme.colorScheme.textWhite.copy(alpha = 0.2f)
                    )
                    .padding(2.dp),
                color = MaterialTheme.colorScheme.textWhite,
                trackColor = Color.Unspecified,
                strokeCap = StrokeCap.Round,
                drawStopIndicator = { }
            )

            Spacer(modifier = Modifier.height(2.dp))

            // todo: should i move the contentDescription value text to xml?
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SmartCounterIconWithDescription(
                    painter = R.drawable.ic_distance_walked,
                    contentDescription = "Distance Walked Icon",
                    valueText = distanceText,
                    supportingText =
                        if (isMetricKm) stringResource(R.string.unit_km)
                        else stringResource(R.string.unit_mi)
                )

                SmartCounterIconWithDescription(
                    painter = R.drawable.ic_weight_scale,
                    contentDescription = "Calorie Burned Icon",
                    valueText = caloriesText,
                    supportingText = stringResource(R.string.unit_kcal)
                )

                SmartCounterIconWithDescription(
                    painter = R.drawable.ic_clock,
                    contentDescription = "Walking Time Icon",
                    valueText = walkingTimeMinutesText,
                    supportingText = stringResource(R.string.unit_min)
                )
            }
        }
    }
}

@Composable
fun SmartCounterIcon(
    painter: Int,
    contentDescription: String? = null,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: (() -> Unit)? = null
) {
    Icon(
        painter = painterResource(painter),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(48.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.textWhite.copy(alpha = 0.2f))

            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )

            .padding(10.dp) // Warning: change padding = change Icon size
    )
}

@Composable
fun SmartCounterIconWithDescription(
    painter: Int,
    contentDescription: String? = null,
    valueText: String,
    supportingText: String,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: (() -> Unit)? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SmartCounterIcon(
            painter = painter,
            contentDescription = contentDescription,
            shape = shape,
            onClick = onClick
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                    ),
                    block = { append(valueText) }
                )
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.textWhite.copy(alpha = 0.7f)
                    ),
                    block = { append(" $supportingText") }
                )
            },
        )
    }
}

@Preview (showBackground = true)
@Composable
fun StepCounterCardPreview() {
    SmartStepTheme {
        StepCounterCard(
            4523,
            6000,
            isPaused = false,
            isMetricKm = true,
            distanceText = "3.2",
            caloriesText = "215",
            walkingTimeMinutesText = "24"
        )
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Preview (showBackground = true)
@Composable
fun StepCounterCardScreenPreview() {
    SmartStepTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepCounterCard(
                4523,
                6000,
                isPaused = false,
                isMetricKm = true,
                distanceText = "3.2",
                caloriesText = "215",
                walkingTimeMinutesText = "24"
            )
        }
    }
}