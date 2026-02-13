package com.seenu.dev.android.smartstep.home.components

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
fun PermissionFirstDenial(
    adaptiveLayoutType: AdaptiveLayoutType,
    modifier: Modifier = Modifier,
) {
    when (adaptiveLayoutType) {
        AdaptiveLayoutType.Mobile -> {
            var showBottomSheet by remember { mutableStateOf(true) }

            FirstDenialBottomSheet(
                showBottomSheet = showBottomSheet,
                onDismissRequest = { showBottomSheet = !showBottomSheet}
            )
        }

        AdaptiveLayoutType.Tablet -> {

        }
    }
}