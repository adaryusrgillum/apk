package com.agentic.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Moss,
    onPrimary = Sand,
    primaryContainer = ColorPalette.primaryContainer,
    onPrimaryContainer = Ink,
    secondary = Clay,
    onSecondary = Sand,
    secondaryContainer = Mist,
    onSecondaryContainer = Ink,
    background = Sand,
    onBackground = Ink,
    surface = Sand,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = Stone
)

private val DarkColors = darkColorScheme(
    primary = Mist,
    onPrimary = Ink,
    primaryContainer = Moss,
    onPrimaryContainer = Sand,
    secondary = Clay,
    onSecondary = Ink,
    background = Ink,
    onBackground = Sand,
    surface = ColorPalette.deepSurface,
    onSurface = Sand,
    surfaceVariant = ColorPalette.deepVariant,
    onSurfaceVariant = Mist
)

private object ColorPalette {
    val primaryContainer = androidx.compose.ui.graphics.Color(0xFFDCE9DF)
    val deepSurface = androidx.compose.ui.graphics.Color(0xFF171615)
    val deepVariant = androidx.compose.ui.graphics.Color(0xFF242220)
}

@Composable
fun AgenticAndroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (false) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
