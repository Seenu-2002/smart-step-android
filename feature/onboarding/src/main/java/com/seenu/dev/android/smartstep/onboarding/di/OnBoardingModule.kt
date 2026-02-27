package com.seenu.dev.android.smartstep.onboarding.di

import com.seenu.dev.android.smartstep.onboarding.presentation.ProfileSetupScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onBoardingModule = module {
    viewModel { ProfileSetupScreenViewModel(get()) }
}