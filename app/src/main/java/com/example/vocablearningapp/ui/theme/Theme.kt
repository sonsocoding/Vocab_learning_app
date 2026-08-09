package com.example.vocablearningapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Accent,
    onPrimary = White,
    primaryContainer = AccentSoft,
    onPrimaryContainer = AccentDark,
    secondary = InkSoft,
    onSecondary = White,
    secondaryContainer = SurfaceMuted,
    onSecondaryContainer = Ink,
    background = Canvas,
    onBackground = Ink,
    surface = Surface,
    onSurface = Ink,
    surfaceVariant = SurfaceMuted,
    onSurfaceVariant = Muted,
    outline = Border,
    error = Forgot,
    onError = White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9AC9B5),
    onPrimary = Color(0xFF12372B),
    primaryContainer = Color(0xFF2C5144),
    onPrimaryContainer = Color(0xFFD5F2E3),
    secondary = Color(0xFFBAC8C0),
    onSecondary = Color(0xFF24332C),
    secondaryContainer = Color(0xFF34443C),
    onSecondaryContainer = Color(0xFFDCE9E1),
    background = Color(0xFF111613),
    onBackground = Color(0xFFE5ECE7),
    surface = Color(0xFF1A211D),
    onSurface = Color(0xFFE5ECE7),
    surfaceVariant = Color(0xFF2B342E),
    onSurfaceVariant = Color(0xFFB8C5BC),
    outline = Color(0xFF455148),
    error = Color(0xFFFFB4A5),
    onError = Color(0xFF5F180E)
)

@Composable
fun VocabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = VocabTypography,
        content = content
    )
}
