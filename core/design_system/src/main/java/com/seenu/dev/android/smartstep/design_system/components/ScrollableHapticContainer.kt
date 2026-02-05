package com.seenu.dev.android.smartstep.design_system.components

import android.util.Log
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundTertiary
import kotlinx.coroutines.launch

@Preview
@Composable
private fun ScrollableHapticContainer_Preview() {
    SmartStepTheme {
        ScrollableHapticContainer(
            options = (1000..10000).step(1000).map { it.toString() },
            visibleCount = 5,
            itemHeight = 20.dp,
            modifier = Modifier,
            content = { _, _ ->
                Text(
                    text = "1000",
                    modifier = Modifier.wrapContentWidth()
                )
            }
        )
    }
}

@Composable
fun ScrollableHapticContainer(
    options: List<String>,
    visibleCount: Int,
    itemHeight: Dp,
    content: @Composable BoxScope.(String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    require(visibleCount % 2 == 1) {
        "Visible count should be an odd number to ensure that the selected item is always in the center."
    }
    Box(
        modifier = modifier.height(
            itemHeight * visibleCount
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = itemHeight)
                .background(color = MaterialTheme.colorScheme.backgroundTertiary)
        )
        val state = rememberLazyListState()
        val snap = rememberSnapFlingBehavior(state)
        val noOfEmptySpaces = visibleCount / 2
        val scope = rememberCoroutineScope()

        var activeIndex by remember {
            mutableStateOf(0)
        }

        LaunchedEffect(state.firstVisibleItemIndex) {
            activeIndex = state.firstVisibleItemIndex
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            state = state,
            flingBehavior = snap
        ) {
            items(noOfEmptySpaces) {
                Box(modifier = Modifier.height(itemHeight))
            }
            items(options.size, key = { index -> options[index] }) { index ->
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .clickable(interactionSource = null, indication = null) {
                            scope.launch {
                                state.animateScrollToItem(index)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    content(options[index], activeIndex == index)
                }
            }
            items(noOfEmptySpaces) {
                Box(modifier = Modifier.height(itemHeight))
            }
        }
    }
}