package com.seenu.dev.android.smartstep.design_system.utils

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

sealed interface AdaptiveLayoutType {
    val isWide: Boolean

    data object Mobile : AdaptiveLayoutType {
        override val isWide = false
    }

    data object Tablet : AdaptiveLayoutType {
        override val isWide = true
    }

    companion object {
        fun from(windowSize: WindowSizeClass): AdaptiveLayoutType {
            return when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Expanded -> Tablet
                else -> Mobile
            }
        }
    }
}