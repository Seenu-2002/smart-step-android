package com.seenu.dev.android.smartstep.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object OnBoardingScreen : Route

    @Serializable
    data object ProfileSetupScreen : Route

    @Serializable
    data object StepCounterScreen : Route

}