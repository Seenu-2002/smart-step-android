package com.seenu.dev.android.smartstep.profile_setup.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.smartstep.design_system.components.HeightSelectionCard
import com.seenu.dev.android.smartstep.design_system.components.SmartStepDropDownField
import com.seenu.dev.android.smartstep.design_system.components.SmartStepDropdown
import com.seenu.dev.android.smartstep.design_system.components.WeightSelectionCard
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.textWhite
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import com.seenu.dev.android.smartstep.design_system.utils.ObserveAsEvents
import com.seenu.dev.android.smartstep.design_system.utils.getString
import com.seenu.dev.android.smartstep.domain.model.Gender
import com.seenu.dev.android.smartstep.profile_setup.R
import org.koin.androidx.compose.koinViewModel


@Preview(name = "Mobile", widthDp = 600)
@Composable
fun MyScreenMobilePreview() {
    SmartStepTheme {
        ProfileSetupScreen(
            AdaptiveLayoutType.Mobile
        )
    }
}

@Preview(name = "Wide", widthDp = 1000)
@Composable
fun MyScreenWidePreview() {
    SmartStepTheme {
        ProfileSetupScreen(
            AdaptiveLayoutType.Tablet
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    adaptiveLayoutType: AdaptiveLayoutType,
    isUserOnboarding: Boolean = false,
    onBack: () -> Unit = {},
    onSetupCompleted: () -> Unit = {}
) {
    val viewModel: ProfileSetupScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.sideEffect) { event ->
        when (event) {
            is ProfileSetupSideEffect.OnProfileSetupComplete -> {
                onSetupCompleted()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_profile),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    if (!isUserOnboarding) {
                        IconButton(
                            onClick = onBack
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = "Back to Home"
                            )
                        }
                    }
                },
                actions = {
                    if (isUserOnboarding) {
                        Text(
                            text = stringResource(R.string.skip),
                            style = MaterialTheme.typography.bodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .clickable {
                                    viewModel.onIntent(ProfileSetupScreenIntent.OnSkip)
                                }
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 10.dp
                                )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.backgroundSecondary
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = if (adaptiveLayoutType.isWide) 394.dp else Dp.Unspecified)
                    .padding(innerPadding)
                    .padding(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.information_msg),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    )
                )

                val shape = MaterialTheme.shapes.medium
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = shape
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = shape
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SmartStepDropdown(
                        selectedOption = stringResource(uiState.gender.getStringRes()),
                        options = Gender.entries.map { stringResource(it.getStringRes()) },
                        onOptionSelected = {
                            val gender = Gender.valueOf(it.uppercase())
                            viewModel.onIntent(
                                ProfileSetupScreenIntent.OnGenderSelected(gender)
                            )
                        },
                        onExpandChange = {
                            viewModel.onIntent(
                                if (it) {
                                    ProfileSetupScreenIntent.ShowGenderSelectionDropDown
                                } else {
                                    ProfileSetupScreenIntent.HideGenderSelectionDropDown
                                }
                            )
                        },
                        isExpanded = uiState.showGenderDropDown,
                        label = "Gender",
                        modifier = Modifier.fillMaxWidth(),
                    )

                    SmartStepDropDownField(
                        text = uiState.heightMetric.getString(),
                        label = stringResource(com.seenu.dev.android.core.design_system.R.string.height),
                        isExpanded = uiState.showHeightSelectionCard,
                        onClick = {
                            viewModel.onIntent(
                                ProfileSetupScreenIntent.ShowHeightSelectionCard
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    SmartStepDropDownField(
                        text = uiState.weightMetric.getString(),
                        label = stringResource(com.seenu.dev.android.core.design_system.R.string.weight),
                        isExpanded = uiState.showWeightSelectionCard,
                        onClick = {
                            viewModel.onIntent(
                                ProfileSetupScreenIntent.ShowWeightSelectionCard
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.weight(1F))

                TextButton(
                    onClick = {
                        viewModel.onIntent(
                            ProfileSetupScreenIntent.OnCompleteProfileSetup
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    val text = if (isUserOnboarding) {
                        R.string.start
                    } else {
                        R.string.save
                    }
                    Text(
                        text = stringResource(text),
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.textWhite
                    )
                }
            }
        }

        if (uiState.showHeightSelectionCard) {
            HeightSelectionCard(
                selectedMetric = uiState.heightMetric,
                onMetricChange = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.OnHeightSelected(it)
                    )
                },
                onMetricTypeChange = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.OnHeightSelected(it)
                    )
                },
                onDismissRequest = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.HideHeightSelectionCard
                    )
                },
                onOk = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.OnHeightSelected(uiState.heightMetric)
                    )
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.HideHeightSelectionCard
                    )
                }
            )
        }



        if (uiState.showWeightSelectionCard) {
            WeightSelectionCard(
                selectedMetric = uiState.weightMetric,
                onMetricChange = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.OnWeightSelected(it)
                    )
                },
                onMetricTypeChange = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.OnWeightSelected(it)
                    )
                },
                onDismissRequest = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.HideWeightSelectionCard
                    )
                },
                onOk = {
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.OnWeightSelected(uiState.weightMetric)
                    )
                    viewModel.onIntent(
                        ProfileSetupScreenIntent.HideWeightSelectionCard
                    )
                }
            )
        }

    }
}

fun Gender.getStringRes(): Int {
    return when (this) {
        Gender.MALE -> R.string.gender_male
        Gender.FEMALE -> R.string.gender_female
    }
}