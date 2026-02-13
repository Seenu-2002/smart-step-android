@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import com.seenu.dev.android.smartstep.home.components.PermissionFirstDenial

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    adaptiveLayoutType: AdaptiveLayoutType,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
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
                                onClick = onMenuClick
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
                PermissionFirstDenial(adaptiveLayoutType)
                Text(text = "Step Counter Component Here :)")
            }
        }

    }
}

@Preview(name = "Mobile", widthDp = 600)
@Composable
fun MyScreenMobilePreview() {
    SmartStepTheme {
        HomeScreen(
            AdaptiveLayoutType.Mobile
        )
    }
}

@Preview(name = "Wide", widthDp = 1000)
@Composable
fun MyScreenWidePreview() {
    SmartStepTheme {
        HomeScreen(
            AdaptiveLayoutType.Tablet
        )
    }
}