package com.seenu.dev.android.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import com.seenu.dev.android.smartstep.navigation.SmartStepNavigation
import com.seenu.dev.android.smartstep.navigation.SmartStepNavigationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val navigationViewModel: SmartStepNavigationViewModel by viewModel()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            navigationViewModel.uiState.value.isLoading
        }
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val adaptiveLayoutType = AdaptiveLayoutType.from(windowSizeClass)
            val navState by navigationViewModel.uiState.collectAsStateWithLifecycle()

            SmartStepTheme {
                if (!navState.isLoading) {
                    SmartStepNavigation(
                        adaptiveLayoutType = adaptiveLayoutType,
                        startRoute = navState.startRoute
                    )
                }
            }
        }
    }
}