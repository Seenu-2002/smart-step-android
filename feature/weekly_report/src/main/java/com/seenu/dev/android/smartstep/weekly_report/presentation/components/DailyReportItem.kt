package com.seenu.dev.android.smartstep.weekly_report.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seenu.dev.android.smartstep.design_system.theme.backgroundTertiary
import com.seenu.dev.android.smartstep.design_system.theme.backgroundWhite
import com.seenu.dev.android.smartstep.design_system.theme.buttonSecondary
import com.seenu.dev.android.smartstep.design_system.theme.strokeMain
import com.seenu.dev.android.smartstep.weekly_report.R
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.DayOfWeek
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.DayOfWeekValues
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.ReportMetric
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.ReportStatus
import java.text.NumberFormat

@Composable
fun DailyReportItem(
    reportMetric: ReportMetric,
    dayOfWeekValues: DayOfWeekValues
) {
    val hasData = dayOfWeekValues.hasData()
    val borderColor = if (dayOfWeekValues.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.strokeMain
    val labelColor = when {
        dayOfWeekValues.isToday -> MaterialTheme.colorScheme.primary
        hasData -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSecondary
    }

    val dayOfWeek = when (dayOfWeekValues.dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(R.string.monday)
        DayOfWeek.TUESDAY -> stringResource(R.string.tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(R.string.wednesday)
        DayOfWeek.THURSDAY -> stringResource(R.string.thursday)
        DayOfWeek.FRIDAY -> stringResource(R.string.friday)
        DayOfWeek.SATURDAY -> stringResource(R.string.saturday)
        DayOfWeek.SUNDAY -> stringResource(R.string.sunday)
    }

    val numberFormat = remember {
        NumberFormat.getInstance().apply {
            maximumFractionDigits = 1
        }
    }

    val formattedValue: String
    val formattedGoal: String
    when (reportMetric) {
        ReportMetric.Distance -> {
            formattedValue = numberFormat.format(dayOfWeekValues.value)
            formattedGoal = numberFormat.format(dayOfWeekValues.goal)
        }
        else -> {
            formattedValue = dayOfWeekValues.value.toInt().toString()
            formattedGoal = dayOfWeekValues.goal?.toInt()?.toString() ?: "No Data"
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.backgroundWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dayOfWeek,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = labelColor
                )

                StatusIcon(dayOfWeekValues.getStatus())
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = labelColor
                            )
                        ) {
                            append(formattedValue)
                        }
                        append(" steps")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = "Goal: $formattedGoal steps",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
fun StatusIcon(status: ReportStatus) {
    val (icon, tint, bg) = when (status) {
        ReportStatus.COMPLETED -> Triple(com.seenu.dev.android.core.design_system.R.drawable.ic_selected, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.buttonSecondary)
        ReportStatus.CURRENT -> Triple(com.seenu.dev.android.core.design_system.R.drawable.ic_clock, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.buttonSecondary)
        ReportStatus.INACTIVE -> Triple(com.seenu.dev.android.core.design_system.R.drawable.ic_minus, MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.backgroundTertiary)
    }

    Box(
        modifier = Modifier
            .size(24.dp)
            .background(bg, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(14.dp)
        )
    }
}