package com.choiminjun.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalColors = staticCompositionLocalOf {
    lightColorScheme
}

private val LocalTypography = staticCompositionLocalOf {
    SRTypography()
}

@Composable
fun SRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkColorScheme
    } else {
        lightColorScheme
    }

    val typography = SRTypography()

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        content = content,
    )
}

object SRTheme {
    val colors: SRColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: SRTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
