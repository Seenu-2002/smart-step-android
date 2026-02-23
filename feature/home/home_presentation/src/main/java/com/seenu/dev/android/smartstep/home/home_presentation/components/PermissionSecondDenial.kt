package com.seenu.dev.android.smartstep.home.home_presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionSecondDenial(
    adaptiveLayoutType: AdaptiveLayoutType,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    dismissable: Boolean = false
) {
    when (adaptiveLayoutType) {
        AdaptiveLayoutType.Mobile -> {
            var showBottomSheet by remember { mutableStateOf(true) }

            ManualPermissionBottomSheet(
                showBottomSheet = showBottomSheet,
                onOpenSettings = onOpenSettings,
                onDismissRequest = { showBottomSheet = !showBottomSheet || !dismissable}
            )
        }

        AdaptiveLayoutType.Tablet -> {

        }
    }
}