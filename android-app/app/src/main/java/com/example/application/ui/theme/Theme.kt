package com.example.application.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(

    primary = BlackPrimary,
    onPrimary = Color.White,

    secondary = AccentYellow,
    onSecondary = BlackPrimary,

    background = LightBackground,
    onBackground = BlackPrimary,

    surface = Color.White,
    onSurface = BlackPrimary,

    error = ErrorRed,
    onError = Color.White
)
@Composable
fun ApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
