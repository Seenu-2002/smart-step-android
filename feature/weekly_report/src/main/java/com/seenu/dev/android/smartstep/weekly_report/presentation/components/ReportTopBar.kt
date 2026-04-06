package com.seenu.dev.android.smartstep.weekly_report.presentation.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.seenu.dev.android.smartstep.design_system.theme.Inter
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Report",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = Inter
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(painterResource(com.seenu.dev.android.core.design_system.R.drawable.ic_arrow_back), contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.backgroundSecondary
        )
    )
}