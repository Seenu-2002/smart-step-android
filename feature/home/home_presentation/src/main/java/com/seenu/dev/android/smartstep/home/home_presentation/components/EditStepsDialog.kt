package com.seenu.dev.android.smartstep.home.home_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.components.SmartStepDropDownField
import com.seenu.dev.android.smartstep.design_system.components.SmartStepNumberField
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.home.home_presentation.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview
@Composable
private fun EditStepsDialog_Preview() {
    SmartStepTheme {
        EditStepsDialog()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStepsDialog(
    modifier: Modifier = Modifier,
    onSave: (Int, String) -> Unit = { _, _ -> },
    onDismissRequest: () -> Unit = {},
) {
    var dateString by remember {
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        mutableStateOf(formatter.format(Date()))
    }
    var stepsInputValue by remember { mutableIntStateOf(0) }
    var showDateSelector by remember {
        mutableStateOf(false)
    }

    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.backgroundSecondary,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Text(
                text = stringResource(R.string.edit_steps),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.edit_steps_msg),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            SmartStepDropDownField(
                text = dateString,
                label = stringResource(R.string.date),
                onClick = { showDateSelector = !showDateSelector },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            SmartStepNumberField(
                initialValue = "0",
                label = stringResource(R.string.steps),
                onValueChange = { stepsInputValue = it.toIntOrNull() ?: 0 },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(com.seenu.dev.android.core.design_system.R.string.cancel),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = onDismissRequest)
                        .padding(
                            10.dp
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = {
                            onSave(
                                stepsInputValue,
                                dateString.replace('/', '-')
                            )
                        })
                        .padding(
                            10.dp
                        )
                )
            }
        }
    }

    if (showDateSelector) {
        DateSelectionCard(
            onDismissRequest = { showDateSelector = false },
            onOk = {
                dateString = it
                showDateSelector = false
            }
        )
    }
}
