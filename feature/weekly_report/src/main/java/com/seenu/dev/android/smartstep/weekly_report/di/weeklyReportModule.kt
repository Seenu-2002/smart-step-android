package com.seenu.dev.android.smartstep.weekly_report.di

import com.seenu.dev.android.smartstep.weekly_report.data.WeeklyReportRepositoryImpl
import com.seenu.dev.android.smartstep.weekly_report.domain.`interface`.WeeklyReportRepository
import com.seenu.dev.android.smartstep.weekly_report.domain.usecase.GetWeeklyReportUseCase
import com.seenu.dev.android.smartstep.weekly_report.presentation.WeeklyReportViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val weeklyReportModule = module {
    single<WeeklyReportRepository> { WeeklyReportRepositoryImpl(get()) }
    factory { GetWeeklyReportUseCase(get()) }
    viewModelOf(::WeeklyReportViewModel)
}