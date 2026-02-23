@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.home.home_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.components.SmartStepBottomSheet
import com.seenu.dev.android.smartstep.design_system.components.SmartStepPrimaryButton
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeRegular
import com.seenu.dev.android.smartstep.home.home_presentation.R

@ExperimentalMaterial3Api
@Composable
fun ManualPermissionBottomSheet(
    showBottomSheet: Boolean,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {}
) {
    SmartStepBottomSheet(
        showBottomSheet = showBottomSheet,
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.enable_access_manually),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.to_track_steps_enable_permission),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLargeRegular,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                StepText(stringResource(R.string.open_permissions))
                StepText(stringResource(R.string.tap_physical_activity))
                StepText(stringResource(R.string.select_allow))
            }

            Spacer(modifier = Modifier.height(32.dp))

            SmartStepPrimaryButton(
                buttonText = stringResource(R.string.open_settings),
                onClick = onOpenSettings
            )
        }
    }
}

@Composable
fun StepText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLargeMedium,
    )
}

@Preview
@Composable
private fun ManualPermissionBottomSheetPreview() {
    SmartStepTheme {
        ManualPermissionBottomSheet(
            true,
            {}
        )
    }
}