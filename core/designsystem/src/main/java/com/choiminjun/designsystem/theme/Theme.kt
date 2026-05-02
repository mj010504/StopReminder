package com.choiminjun.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SemanticPrimary,
    onPrimary = SemanticFgOnPrimary,
    primaryContainer = Blue95,
    onPrimaryContainer = Blue20,
    secondary = CoolNeutral30,
    onSecondary = SemanticFgOnPrimary,
    secondaryContainer = CoolNeutral96,
    onSecondaryContainer = CoolNeutral10,
    tertiary = Violet50,
    onTertiary = SemanticFgOnPrimary,
    tertiaryContainer = Violet95,
    onTertiaryContainer = Violet99,
    background = SemanticBgBase,
    onBackground = SemanticFgPrimary,
    surface = SemanticBgBase,
    onSurface = SemanticFgPrimary,
    surfaceVariant = SemanticBgMuted,
    onSurfaceVariant = SemanticFgSecondary,
    outline = CoolNeutral80,
    outlineVariant = CoolNeutral90,
    error = SemanticNegative,
    onError = SemanticFgOnPrimary,
    errorContainer = SemanticNegativeBg,
    onErrorContainer = Red40,
    inverseSurface = SemanticBgInverse,
    inverseOnSurface = SemanticFgOnInverse,
    inversePrimary = Blue80,
    scrim = SemanticBgScrim,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkFgPrimary,
    primaryContainer = DarkPrimaryBg,
    onPrimaryContainer = Blue90,
    secondary = CoolNeutral70,
    onSecondary = DarkFgPrimary,
    secondaryContainer = CoolNeutral22,
    onSecondaryContainer = CoolNeutral90,
    tertiary = Violet95,
    onTertiary = Violet50,
    background = DarkBgBase,
    onBackground = DarkFgPrimary,
    surface = DarkBgElevated,
    onSurface = DarkFgPrimary,
    surfaceVariant = DarkBgMuted,
    onSurfaceVariant = DarkFgSecondary,
    outline = CoolNeutral40,
    outlineVariant = CoolNeutral30,
    error = DarkNegative,
    onError = DarkBgBase,
    errorContainer = DarkNegativeBg,
    onErrorContainer = Red90,
    inverseSurface = DarkBgInverse,
    inverseOnSurface = CoolNeutral10,
    inversePrimary = Blue50,
)

@Composable
fun StopReminderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = SRTypography,
        shapes = SRShapes,
        content = content,
    )
}
