package com.seenu.dev.android.smartstep.ai_coach.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.ai_coach.presentation.models.AiInsightState
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.backgroundTertiary
import com.seenu.dev.android.smartstep.design_system.theme.backgroundWhite
import com.seenu.dev.android.smartstep.design_system.theme.buttonSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeRegular
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AiInsightsCard(
    insightState: AiInsightState,
    onMoreClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.backgroundWhite)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onMoreClick
            )
            .padding(16.dp)
    ) {
        // Header row: AI icon + action button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AI icon badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.buttonSecondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.seenu.dev.android.smartstep.ai_coach.R.drawable.ic_ai),
                    contentDescription = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.cd_ai),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Action button
            when (insightState) {
                is AiInsightState.Offline -> {
                    Row(
                        modifier = Modifier
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = onTryAgainClick
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.try_again),
                            style = MaterialTheme.typography.bodyLargeRegular,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
                            contentDescription = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.cd_refresh),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(24.dp)
                        )
                    }
                }

                is AiInsightState.Loading -> {
                    // No button while loading
                }

                else -> {
                    Row(
                        modifier = Modifier
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = onMoreClick
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.more),
                            style = MaterialTheme.typography.bodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                            contentDescription = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.cd_more),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content area
        when (insightState) {
            is AiInsightState.Loading -> {
                ShimmerPlaceholder()
            }

            is AiInsightState.Success -> {
                Text(
                    text = insightState.message,
                    style = MaterialTheme.typography.bodyLargeRegular,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            is AiInsightState.Error -> {
                Text(
                    text = insightState.message,
                    style = MaterialTheme.typography.bodyLargeRegular,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            is AiInsightState.Offline -> {
                Text(
                    text = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.offline_connect_for_insights),
                    style = MaterialTheme.typography.bodyLargeRegular,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

// Previews
@Preview(name = "AI Insights - Loading")
@Composable
private fun AiInsightsCardLoadingPreview() {
    SmartStepTheme {
        AiInsightsCard(
            insightState = AiInsightState.Loading,
            onMoreClick = {},
            onTryAgainClick = {},
            modifier = Modifier
        )
    }
}

@Preview(name = "AI Insights - Success")
@Composable
private fun AiInsightsCardSuccessPreview() {
    SmartStepTheme {
        AiInsightsCard(
            insightState = AiInsightState.Success(
                message = "Great job! You’re on track to hit your goal today."
            ),
            onMoreClick = {},
            onTryAgainClick = {},
            modifier = Modifier
        )
    }
}

@Preview(name = "AI Insights - Error")
@Composable
private fun AiInsightsCardErrorPreview() {
    SmartStepTheme {
        AiInsightsCard(
            insightState = AiInsightState.Error(
                message = "Something went wrong. Please try again."
            ),
            onMoreClick = {},
            onTryAgainClick = {},
            modifier = Modifier
        )
    }
}

@Preview(name = "AI Insights - Offline")
@Composable
private fun AiInsightsCardOfflinePreview() {
    SmartStepTheme {
        AiInsightsCard(
            insightState = AiInsightState.Offline,
            onMoreClick = {},
            onTryAgainClick = {},
            modifier = Modifier
        )
    }
}

@Composable
private fun ShimmerPlaceholder() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0F,
        targetValue = 1000F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.backgroundSecondary,
            MaterialTheme.colorScheme.backgroundTertiary,
            MaterialTheme.colorScheme.backgroundSecondary
        ),
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 300f, 0f)
    )

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmerBrush)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmerBrush)
        )
    }
}
