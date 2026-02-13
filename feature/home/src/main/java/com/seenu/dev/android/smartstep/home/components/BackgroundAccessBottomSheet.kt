@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.home.components

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
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeRegular
import com.seenu.dev.android.smartstep.home.R

@ExperimentalMaterial3Api
@Composable
fun BackgroundAccessBottomSheet(
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    SmartStepBottomSheet(
        showBottomSheet = showBottomSheet,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.background_access_recommended),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.background_access_msg),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLargeRegular,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            SmartStepPrimaryButton(
                buttonText = stringResource(R.string.lbl_continue),
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun BackgroundAccessBottomSheetPreview() {
    SmartStepTheme {
        BackgroundAccessBottomSheet(
            true,
            {}
        )
    }
}