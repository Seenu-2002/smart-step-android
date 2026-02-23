@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme

@Composable
fun SmartStepBottomSheet(
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable () -> Unit = {},
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            dragHandle = dragHandle
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun SmartStepBottomSheetPreview() {
    SmartStepTheme {
        SmartStepBottomSheet(
            showBottomSheet = true,
            onDismissRequest = {},
            content = {

            }
        )
    }
}