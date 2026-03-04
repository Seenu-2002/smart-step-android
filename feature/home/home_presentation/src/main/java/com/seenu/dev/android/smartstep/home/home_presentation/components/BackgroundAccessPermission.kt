package com.seenu.dev.android.smartstep.home.home_presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.components.SmartStepBottomSheet
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundAccessPermission(
    adaptiveLayoutType: AdaptiveLayoutType,
    onContinue: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRequestPermission by remember { mutableStateOf(true) }

    when (adaptiveLayoutType) {
        AdaptiveLayoutType.Mobile -> {
            SmartStepBottomSheet(
                showBottomSheet = showRequestPermission,
                onDismissRequest = onDismissRequest,
                modifier = modifier
            ) {
                BackgroundAccessContent(onContinue = onContinue)
            }
        }

        AdaptiveLayoutType.Tablet -> {
            if (showRequestPermission) {
                BasicAlertDialog(
                    onDismissRequest = { 
                        showRequestPermission = !showRequestPermission
                        onDismissRequest()
                    },
                    modifier = modifier
                ) {
                    BackgroundAccessContent(
                        onContinue = onContinue,
                        onCloseClick = {
                            showRequestPermission = !showRequestPermission
                            onDismissRequest() 
                        },
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.large
                        )
                    )
                }
            }
        }
    }
}

@Preview(name = "Mobile w400", widthDp = 400, showBackground = true)
@Preview(name = "Tablet w1000", widthDp = 1000, showBackground = true)
@Composable
private fun BackgroundAccessPermissionPreview() {
    SmartStepTheme {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isTablet = maxWidth >= 600.dp
            val adaptiveLayoutType = if (isTablet) AdaptiveLayoutType.Tablet else AdaptiveLayoutType.Mobile

            BackgroundAccessPermission(
                adaptiveLayoutType = adaptiveLayoutType,
                onContinue = {},
                onDismissRequest = {}
            )
        }
    }
}