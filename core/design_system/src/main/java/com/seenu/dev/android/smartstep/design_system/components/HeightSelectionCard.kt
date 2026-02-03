package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeightSelectionCard(
    value: HeightValue,
    onValueChange: HeightValue,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.backgroundSecondary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(24.dp)
        ) {
            Text(
                text = ""
            )
        }
    }
}

@Stable
sealed interface HeightValue {
    data class Centimeters(val value: Int) : HeightValue
    data class FeetInches(val feet: Int, val inches: Int) : HeightValue
}