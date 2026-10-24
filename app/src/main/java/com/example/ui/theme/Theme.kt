package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VryvColorScheme = darkColorScheme(
    primary = VryvPink,
    onPrimary = Color.White,
    primaryContainer = VryvPinkDark,
    onPrimaryContainer = Color.White,
    secondary = VryvLime,
    onSecondary = Color.Black,
    secondaryContainer = VryvLimeDark,
    onSecondaryContainer = Color.Black,
    tertiary = VryvPinkLight,
    onTertiary = Color.White,
    background = VryvBg,
    onBackground = VryvTextPrimary,
    surface = VryvCard,
    onSurface = VryvTextPrimary,
    surfaceVariant = VryvCardElevated,
    onSurfaceVariant = VryvTextSecondary,
    outline = VryvLine,
    error = VryvError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // VRYV is strictly a dark-themed app as specified
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VryvColorScheme,
        typography = Typography,
        content = content
    )
}
