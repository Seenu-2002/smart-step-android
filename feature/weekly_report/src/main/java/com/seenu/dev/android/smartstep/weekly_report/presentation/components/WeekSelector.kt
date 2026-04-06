package com.seenu.dev.android.smartstep.weekly_report.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.backgroundWhite
import com.seenu.dev.android.smartstep.design_system.theme.buttonSecondary
import com.seenu.dev.android.smartstep.weekly_report.presentation.WeeklyReportAction
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.WeekRange
import java.time.format.DateTimeFormatter

@Composable
fun WeekSelector(
    dateRange: WeekRange,
    onAction: (WeeklyReportAction) -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d") }
    val dateRangeText = remember(dateRange) {
        val startDate = dateRange.start.format(formatter)
        val endDate = dateRange.end.format(formatter)
        "$startDate - $endDate"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onAction(WeeklyReportAction.OnPreviousWeekClicked) },
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(
                painterResource(com.seenu.dev.android.core.design_system.R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.backgroundWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = dateRangeText,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        IconButton(
            onClick = { onAction(WeeklyReportAction.OnNextWeekClicked) },
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (dateRange.hasNextWeek) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.buttonSecondary,
                    shape = CircleShape
                ),
            enabled = dateRange.hasNextWeek
        ) {
            Icon(
                painterResource(com.seenu.dev.android.core.design_system.R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.backgroundWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}