package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium

@Composable
fun SmartStepPrimaryButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Text(
            text = buttonText,
            color = Color.White,
            style = MaterialTheme.typography.bodyLargeMedium
        )
    }
}

@Preview
@Composable
private fun SmartStepPrimaryButtonPreview() {
    SmartStepTheme {
        SmartStepPrimaryButton(
            buttonText = "Next",
            onClick = {}
        )
    }
}