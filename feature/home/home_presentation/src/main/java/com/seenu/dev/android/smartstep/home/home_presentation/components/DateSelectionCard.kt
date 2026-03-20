package com.seenu.dev.android.smartstep.home.home_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
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
import com.seenu.dev.android.smartstep.design_system.components.ScrollableHapticContainer
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.home.home_presentation.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Preview
@Composable
private fun DateSelectionCard_Preview() {
    SmartStepTheme {
        DateSelectionCard()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionCard(
    modifier: Modifier = Modifier,
    initialDate: String? = null,
    onOk: (String) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    var selectedDate by remember {
        mutableStateOf(initialDate ?: SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date()))
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
                text = stringResource(R.string.date),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            DateSelector(
                initialDate = selectedDate,
                onDateChanged = { selectedDate = it }
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
                    text = stringResource(com.seenu.dev.android.core.design_system.R.string.ok),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = { onOk(selectedDate) })
                        .padding(
                            10.dp
                        )
                )
            }
        }
    }
}

@Composable
fun DateSelector(
    initialDate: String,
    onDateChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialValues = remember(initialDate) {
        val parts = initialDate.split("/")
        if (parts.size == 3) {
            Triple(
                parts[0].toIntOrNull() ?: 2026,
                parts[1].toIntOrNull() ?: 1,
                parts[2].toIntOrNull() ?: 1
            )
        } else {
            // Fallback if string is malformed
            val cal = Calendar.getInstance()
            Triple(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
        }
    }

    // 1. Define Ranges
    val yearRange = (2000..2100).toList()
    val monthRange = (1..12).toList()

    // 2. State for current selections
    var selectedYear by remember { mutableIntStateOf(initialValues.first) }
    var selectedMonth by remember { mutableIntStateOf(initialValues.second) } // March
    var selectedDay by remember { mutableIntStateOf(initialValues.third) }

    // 3. Calculate dynamic Day range
    val maxDays = remember(selectedMonth, selectedYear) {
        getDaysInMonth(selectedMonth, selectedYear)
    }
    val dayRange = (1..maxDays).toList()

    // 4. Safety Check: If day is 31 and we switch to Feb, snap back to 28/29
    LaunchedEffect(maxDays) {
        if (selectedDay > maxDays) {
            selectedDay = maxDays
        }
    }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        // YEAR COLUMN
        ScrollableHapticContainer(
            modifier = Modifier.weight(1f),
            options = yearRange.map { it.toString() },
            visibleCount = 5,
            activeIndex = yearRange.indexOf(selectedYear).coerceAtLeast(0),
            onActiveIndexChange = {
                selectedYear = yearRange[it]
                onDateChanged(getDateString(selectedYear, selectedMonth, selectedDay))
            },
            itemHeight = 40.dp,
            content = { text, isSelected -> DateText(text, isSelected) }
        )

        // MONTH COLUMN
        ScrollableHapticContainer(
            modifier = Modifier.weight(1f),
            options = monthRange.map { it.toString().padStart(2, '0') },
            visibleCount = 5,
            activeIndex = monthRange.indexOf(selectedMonth).coerceAtLeast(0),
            onActiveIndexChange = {
                selectedMonth = monthRange[it]
                onDateChanged(getDateString(selectedYear, selectedMonth, selectedDay))
            },
            itemHeight = 40.dp,
            content = { text, isSelected -> DateText(text, isSelected) }
        )

        // DAY COLUMN
        ScrollableHapticContainer(
            modifier = Modifier.weight(1f),
            options = dayRange.map { it.toString() },
            visibleCount = 5,
            activeIndex = dayRange.indexOf(selectedDay).coerceAtLeast(0),
            onActiveIndexChange = {
                selectedDay = dayRange[it]
                onDateChanged(getDateString(selectedYear, selectedMonth, selectedDay))
            },
            itemHeight = 40.dp,
            content = { text, isSelected -> DateText(text, isSelected) }
        )
    }
}

private fun getDateString(year: Int, month: Int, day: Int): String {
    val formattedMonth = month.toString().padStart(2, '0')
    val formattedDay = day.toString().padStart(2, '0')
    return "$year/$formattedMonth/$formattedDay"
}

@Composable
fun DateText(text: String, isSelected: Boolean) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    )
}

fun getDaysInMonth(month: Int, year: Int): Int {
    return when (month) {
        2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
}
