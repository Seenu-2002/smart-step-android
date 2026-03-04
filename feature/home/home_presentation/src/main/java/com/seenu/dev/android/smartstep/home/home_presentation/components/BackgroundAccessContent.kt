@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.home.home_presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.components.SmartStepPrimaryButton
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeRegular
import com.seenu.dev.android.smartstep.home.home_presentation.R
import com.seenu.dev.android.core.design_system.R as RDrawable


@Composable
fun BackgroundAccessContent(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    onCloseClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
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
            onClick = onContinue
        )
    }

    if (onCloseClick != null) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(RDrawable.drawable.ic_close),
                    contentDescription = stringResource(id = android.R.string.cancel)
                )
            }
        }
    }
}