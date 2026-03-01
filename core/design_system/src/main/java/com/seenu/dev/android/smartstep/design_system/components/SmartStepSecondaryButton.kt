package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.strokeMain
import com.seenu.dev.android.smartstep.design_system.theme.textWhite

@Composable
fun SmartStepSecondaryButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.bodyLargeMedium
        )
    }
}

@Preview
@Composable
private fun SmartStepSecondaryButtonPreview() {
    SmartStepTheme {
        SmartStepSecondaryButton(
            buttonText = "Cancel",
            onClick = {}
        )
    }
}