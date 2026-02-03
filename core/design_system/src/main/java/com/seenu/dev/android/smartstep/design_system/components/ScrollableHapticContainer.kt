package com.seenu.dev.android.smartstep.design_system.components

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme

@Preview
@Composable
private fun ScrollableHapticContainer_Preview() {
    SmartStepTheme {
        ScrollableHapticContainer(
            range = 0..100,
            step = 5,
            visibleCount = 5,
            modifier = Modifier
        )
    }
}

@Composable
fun ScrollableHapticContainer(
    range: IntRange,
    step: Int,
    visibleCount: Int,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()
    val snap = rememberSnapFlingBehavior(state)
    var contentHeight by remember {
        mutableStateOf(0)
    }
    val height = with(LocalDensity.current) {
        (visibleCount * contentHeight).toDp()
    }
    LazyColumn(
        modifier = modifier.let {
            if (contentHeight != 0) {
                it.height(height)
            } else {
                it
            }
        },
        state = state,
        flingBehavior = snap
    ) {
        val values = range.step(step).toList()
        items(values.size, key = { index -> values[index] }) { index ->
            Text(
                text = values[index].toString(),
                maxLines = 1,
                modifier = Modifier.onGloballyPositioned {
                    if (index == 0) {
                        contentHeight = it.size.height
                    }
                }
            )
        }
    }
}