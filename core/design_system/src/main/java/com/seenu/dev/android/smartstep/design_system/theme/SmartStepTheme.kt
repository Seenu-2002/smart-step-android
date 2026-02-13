package com.seenu.dev.android.smartstep.design_system.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.seenu.dev.android.core.design_system.R

val Inter = FontFamily(
    Font(R.font.inter_regular, weight = FontWeight.Normal),
    Font(R.font.inter_medium, weight = FontWeight.Medium),
    Font(R.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(R.font.inter_bold, weight = FontWeight.Bold),
)

val Typography.title: TextStyle
    get() = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 64.sp,
        lineHeight = 70.sp
    )

val Typography.bodyLargeMedium: TextStyle
    get() = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

val Typography.bodyMediumMedium: TextStyle
    get() = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

val ColorScheme.buttonSecondary: Color
    get() = ButtonSecondary

val ColorScheme.textWhite: Color
    get() = TextWhite

val ColorScheme.backgroundSecondary: Color
    get() = BackgroundSecondary

val ColorScheme.backgroundTertiary: Color
    get() = BackgroundTertiary

val ColorScheme.backgroundWhite: Color
    get() = BackgroundWhite

val ColorScheme.backgroundWhite20: Color
    get() = BackgroundWhite20

@Composable
fun SmartStepTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = ButtonPrimary,
            onPrimary = TextPrimary,
            onSecondary = TextSecondary,
            background = BackgroundMain,
            surface = Surface,
            outline = StrokeMain
        ),
        typography = MaterialTheme.typography.copy(
            titleMedium = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Medium
            ),
            bodyLarge = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Normal
            ),
            bodyMedium = TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Normal
            ),
            bodySmall = TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Normal
            )
        ),
        content = content
    )
}