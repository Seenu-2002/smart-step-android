package com.seenu.dev.android.smartstep.weekly_report.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import com.seenu.dev.android.smartstep.weekly_report.presentation.components.DailyReportItem
import com.seenu.dev.android.smartstep.weekly_report.presentation.components.ReportSummaryCard
import com.seenu.dev.android.smartstep.weekly_report.presentation.components.ReportTopBar
import com.seenu.dev.android.smartstep.weekly_report.presentation.components.WeekSelector
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.DayOfWeek
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.DayOfWeekValues
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.ReportMetric
import com.seenu.dev.android.smartstep.weekly_report.presentation.utils.WeekRangeUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeeklyReportScreen(
    adaptiveLayoutType: AdaptiveLayoutType,
    onNavigateBack: () -> Unit
) {
    val viewModel:  WeeklyReportViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeeklyReportScreenRoot(
        adaptiveLayoutType = adaptiveLayoutType,
        uiState = uiState,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun WeeklyReportScreenRoot(
    adaptiveLayoutType: AdaptiveLayoutType,
    uiState: WeeklyReportUiState,
    onAction: (WeeklyReportAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            ReportTopBar(onNavigateBack)
        },
        containerColor = MaterialTheme.colorScheme.backgroundSecondary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.backgroundSecondary),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (uiState.isLoading) {

            }

            val valuesUnit = stringResource(uiState.selectedMetric.unitRes)
            ReportSummaryCard(
                uiState.selectedMetric,
                uiState.totalWeeklyValue,
                uiState.dailyAverageValue,
                valuesUnit
            )

            WeekSelector(uiState.weekRange, onAction)

            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.weekValues.forEach { dayOfWeekValues ->
                    DailyReportItem(uiState.selectedMetric, dayOfWeekValues)
                }
            }
        }
    }
}


@Preview
@Composable
private fun WeeklyReportScreenPreview() {
    SmartStepTheme {
        WeeklyReportScreenRoot(
            adaptiveLayoutType = AdaptiveLayoutType.Mobile,
            onNavigateBack = {},
            onAction = {},
            uiState = WeeklyReportUiState(
                selectedMetric = ReportMetric.Steps,
                totalWeeklyValue = 123.0,
                dailyAverageValue = 35.0,
                weekRange = WeekRangeUtils.getCurrentWeekRange(),
                weekValues = listOf(
                    DayOfWeekValues(
                        dayOfWeek = DayOfWeek.MONDAY,
                        value = 123.0,
                        goal = 2000.0,
                        isToday = false
                    ),
                    DayOfWeekValues(
                        dayOfWeek = DayOfWeek.TUESDAY,
                        value = 153.0,
                        goal = 2000.0,
                        isToday = true
                    ),
                    DayOfWeekValues(
                        dayOfWeek = DayOfWeek.WEDNESDAY,
                        value = 183.0,
                        goal = 2000.0,
                        isToday = false
                    ),
                    DayOfWeekValues(
                        dayOfWeek = DayOfWeek.THURSDAY,
                        value = 243.0,
                        goal = 2000.0,
                        isToday = false
                    ),
                    DayOfWeekValues(
                        dayOfWeek = DayOfWeek.FRIDAY,
                        value = 0.0,
                        goal = 2000.0,
                        isToday = false
                    ),
                ),
                isLoading = false
            ),
        )
    }
}