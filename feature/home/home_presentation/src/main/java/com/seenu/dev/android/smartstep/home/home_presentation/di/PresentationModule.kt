package com.seenu.dev.android.smartstep.home.home_presentation.di

import com.seenu.dev.android.smartstep.home.home_presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homePresentationModule = module {
    viewModel { HomeViewModel(get(), get()) }
}