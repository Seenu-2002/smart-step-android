package com.seenu.dev.android.smartstep.profile_setup.di

import com.seenu.dev.android.smartstep.profile_setup.presentation.ProfileSetupScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileSetupModule = module {
    viewModel {
        ProfileSetupScreenViewModel(
            userConfigRepository = get()
        )
    }
}