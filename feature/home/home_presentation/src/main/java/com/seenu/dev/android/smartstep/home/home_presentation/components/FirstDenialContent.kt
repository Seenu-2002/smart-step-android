@file:OptIn(ExperimentalMaterial3Api::class)

package com.seenu.dev.android.smartstep.home.home_presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.components.SmartStepPrimaryButton
import com.seenu.dev.android.smartstep.design_system.theme.bottomSheetImageBorder
import com.seenu.dev.android.smartstep.home.home_presentation.R

@Composable
fun FirstDenialContent(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.bottomSheetImageBorder,
                    shape = RoundedCornerShape(8.dp)
                )
                .size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_pin_direction),
                contentDescription = "FirstDenialBottomSheetIcon"
            )
        }
        Text(
            text = stringResource(R.string.to_count_steps_access_motion_sensors),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 44.dp)
        )
        SmartStepPrimaryButton(
            buttonText = stringResource(R.string.allow_access),
            onClick = onButtonClick
        )
    }
}