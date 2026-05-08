package com.choiminjun.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.blue50,
            secondary = colors.coolNeutral40,
            background = colors.background,
            surface = colors.background,
        )
    } else {
        lightColorScheme(
            primary = colors.blue50,
            secondary = colors.coolNeutral40,
            background = colors.background,
            surface = colors.background,
        )
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
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
