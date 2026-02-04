package com.seenu.dev.android.smartstep.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.seenu.dev.android.smartstep.onboarding.ProfileSetupScreen
import kotlin.collections.listOf

@Composable
fun SmartStepNavigation() {
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
                            onSkip = {
                                TODO()
                            }
                        )
                    }
                }

                else -> TODO()
            }
        }
    )
}