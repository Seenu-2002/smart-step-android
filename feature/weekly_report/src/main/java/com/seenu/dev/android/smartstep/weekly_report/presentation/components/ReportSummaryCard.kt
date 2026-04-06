package com.seenu.dev.android.smartstep.weekly_report.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.textWhite
import com.seenu.dev.android.smartstep.weekly_report.R
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.ReportMetric
import java.text.NumberFormat

@Composable
fun ReportSummaryCard(
    reportMetric: ReportMetric,
    totalValue: Double,
    dailyAverage: Double,
    valuesUnit: String
) {
    val numberFormat = remember {
        NumberFormat.getInstance().apply {
            maximumFractionDigits = 1
        }
    }

    val formattedTotalValue: String
    val formattedDailyAverage: String
    when (reportMetric) {
        ReportMetric.Distance -> {
            formattedTotalValue = numberFormat.format(totalValue)
            formattedDailyAverage = numberFormat.format(dailyAverage)
        }
        else -> {
            formattedTotalValue = totalValue.toInt().toString()
            formattedDailyAverage = dailyAverage.toInt().toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(reportMetric.nameRes),
                color = MaterialTheme.colorScheme.textWhite,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "This Week",
                color = MaterialTheme.colorScheme.textWhite,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = formattedTotalValue,
            color = MaterialTheme.colorScheme.textWhite,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = stringResource(
                R.string.daily_average,
                formattedDailyAverage,
                valuesUnit
            ),
            color = MaterialTheme.colorScheme.textWhite,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}