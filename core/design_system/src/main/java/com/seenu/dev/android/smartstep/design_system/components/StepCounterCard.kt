package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.theme.Inter
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.textWhite
import com.seenu.dev.android.smartstep.design_system.theme.title

@Composable
fun StepCounterCard(
    currentStepCount: Int,
    targetStepCount: Int
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

            Icon(
                painter = painterResource(R.drawable.ic_sneakers),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.textWhite.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(10.dp) // carefull the padding reduce the Icon size
            )

            Text(
                text = stringResource(R.string.current_steps, currentStepCount),
                style = MaterialTheme.typography.title,
                fontFamily = Inter
            )

            Text(
                text = stringResource(R.string.target_steps, targetStepCount),
                style = MaterialTheme.typography.bodyLargeMedium,
            )

            // the dot in the end of the LinearProgressIndicator come from material3
            LinearProgressIndicator(
                progress = { (currentStepCount.toFloat() / targetStepCount.toFloat()).coerceIn(0f, 1f) },
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
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun StepCounterCardPreview() {
    SmartStepTheme {
        StepCounterCard(4523, 6000)
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
            StepCounterCard(4523, 6000)
        }
    }
}