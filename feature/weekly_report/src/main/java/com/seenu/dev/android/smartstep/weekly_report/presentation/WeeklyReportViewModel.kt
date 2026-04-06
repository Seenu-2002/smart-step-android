package com.seenu.dev.android.smartstep.weekly_report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.smartstep.weekly_report.domain.model.Metrics
import com.seenu.dev.android.smartstep.weekly_report.domain.usecase.GetWeeklyReportUseCase
import com.seenu.dev.android.smartstep.weekly_report.presentation.mapper.toDayOfWeekValues
import com.seenu.dev.android.smartstep.weekly_report.presentation.models.WeekRange
import com.seenu.dev.android.smartstep.weekly_report.presentation.utils.WeekRangeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeeklyReportViewModel(
    val getWeeklyReportUseCase: GetWeeklyReportUseCase
) : ViewModel() {

    private var loadWeeklyReportJob: Job? = null
    private val _uiState = MutableStateFlow(WeeklyReportUiState())
    val uiState: StateFlow<WeeklyReportUiState> = _uiState.asStateFlow()


    fun onAction(action: WeeklyReportAction) {
        when (action) {
            WeeklyReportAction.OnNextWeekClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                val currentWeekRange = _uiState.value.weekRange
                val nextWeekRange = WeekRangeUtils.getNextWeekRange(currentWeekRange)
                nextWeekRange?.let {
                    loadWeeklyReport(it)
                }
            }
            WeeklyReportAction.OnPreviousWeekClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                val currentWeekRange = _uiState.value.weekRange
                val previousWeekRange = WeekRangeUtils.getPreviousWeekRange(currentWeekRange)
                loadWeeklyReport(previousWeekRange)
            }
        }
    }

    init {
        loadWeeklyReport(WeekRangeUtils.getCurrentWeekRange())
    }

    fun loadWeeklyReport(weekRange: WeekRange) {
        loadWeeklyReportJob?.cancel()
        loadWeeklyReportJob = viewModelScope.launch {
            val weeklyReport =  getWeeklyReportUseCase(
                startDate = weekRange.start,
                endDate = weekRange.end,
                metric = Metrics.STEPS
            )

            _uiState.update {
                it.copy(
                    totalWeeklyValue = weeklyReport.totalValue,
                    dailyAverageValue = weeklyReport.averageValue,
                    weekValues = weeklyReport.dailyReports.toDayOfWeekValues(),
                    weekRange = weekRange,
                    isLoading = false
                )
            }
        }
    }

}