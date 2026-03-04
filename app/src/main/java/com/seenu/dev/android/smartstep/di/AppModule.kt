package com.seenu.dev.android.smartstep.di

import com.seenu.dev.android.smartstep.navigation.SmartStepNavigationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SmartStepNavigationViewModel(get()) }
}

