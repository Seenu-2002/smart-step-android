package com.seenu.dev.android.smartstep.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import com.seenu.dev.android.smartstep.navigation.SmartStepNavigationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Application-level CoroutineScope for long-lived background tasks (DI-provided, not GlobalScope)
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    viewModel { SmartStepNavigationViewModel(get()) }
}

