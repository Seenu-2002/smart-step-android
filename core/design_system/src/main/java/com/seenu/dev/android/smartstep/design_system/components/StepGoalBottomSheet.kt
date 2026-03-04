package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary

@Preview(name = "Mobile w400", widthDp = 400, showBackground = true)
@Preview(name = "Tablet w1000", widthDp = 1000, showBackground = true)
@Composable
private fun StepGoalBottomSheet_Preview() {
    SmartStepTheme {
        StepGoalBottomSheet(
            currentValue = 5000,
            onSave = {},
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepGoalBottomSheet(
    currentValue: Int,
    modifier: Modifier = Modifier,
    range: IntRange = 1000..40000,
    step: Int = 1000,
    onSave: (Int) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    require(currentValue in range) { "currentValue must be within the provided range." }
    val options = remember(range, step) {
        (range.first..range.last).step(step).toList().map { it.toString() }
    }
    var activeIndex by remember {
        mutableIntStateOf(options.indexOf(currentValue.toString()))
    }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        dragHandle = {},
        containerColor = MaterialTheme.colorScheme.backgroundSecondary,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = stringResource(R.string.step_goal),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            ScrollableHapticContainer(
                options = options,
                activeIndex = activeIndex,
                onActiveIndexChange = {
                    activeIndex = it
                },
                visibleCount = 5,
                itemHeight = 40.dp,
                content = { text, isSelected ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                    )
                }
            )

            SmartStepPrimaryButton(
                buttonText = stringResource(R.string.save),
                onClick = {
                    onSave(options[activeIndex].toInt())
                },
                modifier = Modifier.fillMaxWidth(),
            )


            SmartStepSecondaryButton(
                buttonText = stringResource(R.string.cancel),
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}