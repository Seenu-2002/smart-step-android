@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.home.home_presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.seenu.dev.android.smartstep.design_system.components.ExitConfirmationDialog
import com.seenu.dev.android.smartstep.design_system.components.SmartStepNavigationDrawer
import com.seenu.dev.android.smartstep.design_system.components.StepCounterCard
import com.seenu.dev.android.smartstep.design_system.components.StepGoalBottomSheet
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import com.seenu.dev.android.smartstep.home.home_presentation.components.BackgroundAccessPermission
import com.seenu.dev.android.smartstep.home.home_presentation.components.ObserveOnResume
import com.seenu.dev.android.smartstep.home.home_presentation.components.PermissionFirstDenial
import com.seenu.dev.android.smartstep.home.home_presentation.components.PermissionSecondDenial
import com.seenu.dev.android.smartstep.home.home_presentation.extensions.findActivity
import com.seenu.dev.android.smartstep.home.home_presentation.extensions.openAppSettings
import com.seenu.dev.android.smartstep.home.home_presentation.utils.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("InlinedApi", "UseKtx", "BatteryLife")
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    adaptiveLayoutType: AdaptiveLayoutType,
    modifier: Modifier = Modifier,
    onNavigatePersonalSettingsClick: () -> Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle()
    val events = homeViewModel.events

    val context = LocalContext.current
    // Safely find the Activity from the context
    val activity = remember(context) { context.findActivity() }

    val activityRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        homeViewModel.onAction(HomeAction.OnActivityRecognitionPermissionUpdate(granted))
    }

    ObserveAsEvents(events) { event ->
        when (event) {
            HomeEvent.OnActivityRecognitionPermissionRequired -> {
                val rationaleRequired = activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    )
                } ?: false

                if (rationaleRequired) {
                    homeViewModel.onAction(HomeAction.RationaleRequired)
                }
                activityRecognitionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }

            HomeEvent.OnBackgroundPermissionRequired -> {
                val intent = Intent(
                    Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    "package:${context.packageName}".toUri()
                )
                context.startActivity(intent)
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            homeViewModel.onAction(HomeAction.OnPermissionUpdateRequired)
        }
    }

    ObserveOnResume(onResume = {
        homeViewModel.onAction(HomeAction.CheckIsIgnoringBatteryOptimizations)
    })

    HomeScreenRoot(
        adaptiveLayoutType = adaptiveLayoutType,
        uiState = uiState.value,
        onAction = homeViewModel::onAction,
        onNavigatePersonalSettingsClick = onNavigatePersonalSettingsClick,
        modifier = modifier,
    )
}

@SuppressLint("InlinedApi")
@Composable
fun HomeScreenRoot(
    adaptiveLayoutType: AdaptiveLayoutType,
    uiState: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigatePersonalSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SmartStepNavigationDrawer(
        showFixIssueItem = !uiState.isIgnoringBatteryOptimizations,
        onFixIssueClick = {
            scope.launch { drawerState.close() }
            onAction(HomeAction.OnFixStopCountingStepIssueClick)
        },
        onStepGoalClick = {
            scope.launch { drawerState.close() }
            onAction(HomeAction.ShowStepGoalSheet)
        },
        onPersonalSettingsClick = {
            scope.launch { drawerState.close() }
            onNavigatePersonalSettingsClick()
        },
        onExitClick = {
            scope.launch { drawerState.close() }
            onAction(HomeAction.ShowExitConfirmationDialog)
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.smart_step),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(com.seenu.dev.android.core.design_system.R.drawable.ic_menu),
                            contentDescription = "Menu",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }
                                )
                                .padding(start = 16.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.backgroundSecondary
        ) { innerPadding ->

            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = if (adaptiveLayoutType.isWide) 394.dp else Dp.Unspecified)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (uiState.activityRecognitionPermissionGranted) {
                        StepCounterCard(
                            currentStepCount = uiState.currentSteps,
                            targetStepCount = uiState.stepGoal,
                            // Pass the new calculated metrics
                            distanceText = uiState.distanceText,
                            caloriesText = uiState.caloriesText,
                            walkingTimeMinutesText = uiState.minutesText,
                            // Pass the pause state and actions
                            isPaused = uiState.isPaused,
                            onPausePlayIconClick = { onAction(HomeAction.OnPausePlayIconClick) },
                            onPenIconClick = { onAction(HomeAction.OnEditStepsClick) }
                        )
                    } else {
                        uiState.permissionDenialStep?.let {
                            when (uiState.permissionDenialStep) {
                                DenialStep.FIRST_DENIAL -> {
                                    PermissionFirstDenial(
                                        adaptiveLayoutType = adaptiveLayoutType,
                                        onAllowAccessClick = { onAction(HomeAction.OnRequireActivityRecognitionPermission) }
                                    )
                                }

                                DenialStep.SECOND_DENIAL -> {
                                    PermissionSecondDenial(
                                        adaptiveLayoutType = adaptiveLayoutType,
                                        onOpenSettings = { context.openAppSettings() },
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.showBackgroundAccessRecommended) {
                        BackgroundAccessPermission(
                            adaptiveLayoutType = adaptiveLayoutType,
                            onContinue = { onAction(HomeAction.OnBackgroundAccessRecommendedContinue) },
                            onDismissRequest = { onAction(HomeAction.OnBackgroundAccessRecommendedDismiss) }
                        )
                    }

                    if (uiState.showExitConfirmationDialog) {
                        val activity = LocalActivity.current
                        ExitConfirmationDialog(
                            onExit = {
                                activity?.finishAffinity()
                            },
                            onDismissRequest = {
                                onAction(HomeAction.DismissExitConfirmationDialog)
                            }
                        )
                    }

                    if (uiState.showStepGoalSheet) {
                        StepGoalBottomSheet(
                            currentValue = uiState.stepGoal,
                            onSave = {
                                onAction(HomeAction.UpdateStepGoal(it))
                            },
                            onDismissRequest = {
                                onAction(HomeAction.DismissStepGoalSheet)
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(name = "Mobile", widthDp = 600)
@Composable
fun MyScreenMobilePreview() {
    SmartStepTheme {
        HomeScreenRoot(
            AdaptiveLayoutType.Mobile,
            uiState = HomeState(isFirstInstall = true),
            onNavigatePersonalSettingsClick = {},
            onAction = {}
        )
    }
}

@Preview(name = "Wide", widthDp = 1000)
@Composable
fun MyScreenWidePreview() {
    SmartStepTheme {
        HomeScreenRoot(
            AdaptiveLayoutType.Tablet,
            uiState = HomeState(),
            onNavigatePersonalSettingsClick = {},
            onAction = {}
        )
    }
}