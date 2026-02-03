package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary

@Preview
@Composable
private fun SmartStepDropdownField_Preview() {
    SmartStepTheme {
        var selected by remember {
            mutableStateOf("Demo")
        }
        SmartStepDropDownField(
            selected = selected,
            label = "Weight"
        )
    }
}

@Composable
fun SmartStepDropDownField(
    selected: String,
    label: String,
    isExpanded: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = MaterialTheme.shapes.medium
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.backgroundSecondary,
                shape = shape,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .clickable(onClick = onClick)
            .clip(shape = shape)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1F), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = selected,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_arrow_down),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.graphicsLayer {
                if (isExpanded) {
                    rotationZ = 180F
                }
            }
        )
    }
}

@Preview
@Composable
private fun SmartStepDropdown_Preview() {
    SmartStepTheme {
        SmartStepDropdown(
            selectedOption = "Option 2",
            label = "Test",
            options = listOf("Option 1", "Option 2", "Option 3"),
            isExpanded = true,
            onOptionSelected = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartStepDropdown(
    selectedOption: String,
    options: List<String>,
    label: String,
    isExpanded: Boolean,
    onOptionSelected: (String) -> Unit,
    onExpandChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    require(options.contains(selectedOption)) {
        "Selected option must be present in the options list"
    }
    ExposedDropdownMenuBox(
        modifier = modifier
            .dropShadow(
                shape = MaterialTheme.shapes.medium,
                shadow = androidx.compose.ui.graphics.shadow.Shadow(
                    color = Color(0x29101828),
                    radius = 16.dp,
                    spread = -(4).dp,
                    offset = DpOffset(0.dp, 12.dp)
                )
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium),
        expanded = isExpanded,
        onExpandedChange = onExpandChange
    ) {
        SmartStepDropDownField(
            selected = selectedOption,
            label = label,
            isExpanded = isExpanded,
            modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                onExpandChange(false)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            for (option in options) {
                val isSelected = option == selectedOption
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .let {
                            if (isSelected) {
                                it.background(
                                    color = MaterialTheme.colorScheme.backgroundSecondary,
                                    shape = MaterialTheme.shapes.medium
                                )
                            } else {
                                it
                            }
                        }
                        .clickable {
                            onOptionSelected(option)
                        }
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    if (isSelected) {
                        Icon(
                            painter = painterResource(R.drawable.ic_selected),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}