@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import com.seenu.dev.android.smartstep.home.HomeScreen
import com.seenu.dev.android.smartstep.onboarding.ProfileSetupScreen

@Composable
fun SmartStepNavigation(
    adaptiveLayoutType: AdaptiveLayoutType
) {
    val backstack = rememberNavBackStack(Route.ProfileSetupScreen)
    NavDisplay(
        backStack = backstack,
        modifier = Modifier.fillMaxSize(),
        onBack = { backstack.removeLastOrNull() },
        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
        entryProvider = { key ->
            when (key) {
                Route.ProfileSetupScreen -> {
                    NavEntry(key) {
                        ProfileSetupScreen(
                            adaptiveLayoutType = adaptiveLayoutType,
                            onSkip = {
                                TODO()
                            }
                        )
                    }
                }

                Route.StepCounterScreen -> {
                    NavEntry(key) {
                        HomeScreen(
                            adaptiveLayoutType = adaptiveLayoutType,
                        )
                    }
                }

                else -> TODO()
            }
        }
    )
}