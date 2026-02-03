package com.seenu.dev.android.smartstep.onboarding

import android.R.attr.label
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.components.SmartStepDropDownField
import com.seenu.dev.android.smartstep.design_system.components.SmartStepDropdown
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.textWhite

@Preview
@Composable
private fun ProfileSetupScreen_Preview() {
    SmartStepTheme {
        ProfileSetupScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(onSkip: () -> Unit = {}) {
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
                actions = {
                    Text(
                        text = stringResource(R.string.skip),
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .clickable(onClick = onSkip)
                            .padding(
                                horizontal = 16.dp,
                                vertical = 10.dp
                            )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.backgroundSecondary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
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
                repeat(3) {
                    var isExpanded by remember {
                        mutableStateOf(false)
                    }
//                    Box(modifier = Modifier.fillMaxWidth()) {
//                        SmartStepDropDownField(
//                            selected = "Female",
//                            label = "Gender",
//                            isExpanded = isExpanded,
//                            modifier = Modifier.fillMaxWidth(),
//                            onClick = {
//                                isExpanded = !isExpanded
//                            }
//                        )
                        SmartStepDropdown(
                            selectedOption = "Female",
                            options = listOf("Male", "Female"),
                            onOptionSelected = { isExpanded = false },
                            onExpandChange = {
                                isExpanded = it
                            },
                            isExpanded = isExpanded,
                            label = "Gender",
                            modifier = Modifier.fillMaxWidth()
                        )
//                    }
                }
            }

            Spacer(modifier = Modifier.weight(1F))

            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.start),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.textWhite
                )
            }
        }
    }
}