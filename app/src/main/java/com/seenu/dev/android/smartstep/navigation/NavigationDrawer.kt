package com.seenu.dev.android.smartstep.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.strokeMain
import kotlinx.coroutines.launch

@Composable
fun SmartStepDrawerContent(
    onFixIssueClick: () -> Unit,
    onStepGoalClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onExitClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.backgroundSecondary)
                .padding(16.dp)
        ) {
            DrawerItem(
                text = stringResource(R.string.fix_the_stop_counting_steps_issue),
                onClick = onFixIssueClick,
            )
            DrawerItem(
                text = stringResource(R.string.step_goal),
                onClick = onStepGoalClick,
            )
            DrawerItem(
                text = stringResource(R.string.personal_settings),
                onClick = onSettingsClick,
            )
            DrawerItem(
                text = stringResource(R.string.exit),
                onClick = onExitClick,
                textColor = MaterialTheme.colorScheme.primary,
                borderColor = Color.Transparent
            )
        }
    }
}

// move this to dimens.xml?
private val DrawerItemHeight = 56.dp
private val DrawerItemStrokeWidth = 1.dp

@Composable
private fun DrawerItem(
    text: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = MaterialTheme.colorScheme.strokeMain
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(DrawerItemHeight)
            .clickable(onClick = onClick)
            .drawBehind {
                if (borderColor.alpha > 0f) {
                    val strokeThickness = DrawerItemStrokeWidth.toPx()
                    val y = size.height - (strokeThickness / 2)

                    drawLine(
                        color = borderColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeThickness
                    )
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(name = "Mobile w400", widthDp = 400, showBackground = true)
@Preview(name = "Tablet w1000", widthDp = 1000, showBackground = true)
@Composable
fun SmartStepDrawerOpenPreview() {
    SmartStepTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                SmartStepDrawerContent(
                    onFixIssueClick = {},
                    onStepGoalClick = {},
                    onSettingsClick = {},
                    onExitClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
        ) {
            Scaffold(
                topBar = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = "Menu"
                        )
                    }
                }
            ) { padding ->
                Box(
                    modifier = Modifier.padding(padding)
                ) {
                    Text("Main App Content Background", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}