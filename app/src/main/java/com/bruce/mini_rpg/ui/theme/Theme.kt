package com.bruce.mini_rpg.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PixelDarkColorScheme = darkColorScheme(
    primary = PixelLightGreen,
    onPrimary = PixelBlack,
    primaryContainer = PixelDarkGreen,
    onPrimaryContainer = PixelWhite,
    secondary = PixelGold,
    onSecondary = PixelBlack,
    secondaryContainer = PixelCopper,
    onSecondaryContainer = PixelWhite,
    tertiary = PixelBlue,
    onTertiary = PixelWhite,
    background = PixelBlack,
    onBackground = PixelWhite,
    surface = PixelDarkGray,
    onSurface = PixelWhite,
    surfaceVariant = PixelGray,
    onSurfaceVariant = PixelLightGray,
    outline = PixelLightGray,
    error = PixelRed,
    onError = PixelWhite
)

private val PixelLightColorScheme = lightColorScheme(
    primary = PixelDarkGreen,
    onPrimary = PixelWhite,
    primaryContainer = PixelLightGreen,
    onPrimaryContainer = PixelBlack,
    secondary = PixelCopper,
    onSecondary = PixelWhite,
    secondaryContainer = PixelGold,
    onSecondaryContainer = PixelBlack,
    tertiary = PixelBlue,
    onTertiary = PixelWhite,
    background = PixelWhite,
    onBackground = PixelBlack,
    surface = PixelLightGray,
    onSurface = PixelBlack,
    surfaceVariant = PixelGray,
    onSurfaceVariant = PixelDarkGray,
    outline = PixelDarkGray,
    error = PixelRed,
    onError = PixelWhite
)

@Composable
fun MiniRPGTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        PixelDarkColorScheme
    } else {
        PixelLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}