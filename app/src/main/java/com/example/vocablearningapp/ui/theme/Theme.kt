package com.example.vocablearningapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = QuizletBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDF0FF),
    onPrimaryContainer = QuizletNavy,
    secondary = QuizletNavy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8EBFF),
    onSecondaryContainer = QuizletNavy,
    tertiary = QuizletPurple,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF3E5F5),
    onTertiaryContainer = Color(0xFF4A148C),
    background = QuizletBackground,
    onBackground = QuizletNavy,
    surface = QuizletCardBg,
    onSurface = QuizletNavy,
    surfaceVariant = Color(0xFFEFF2FB),
    onSurfaceVariant = Color(0xFF586380),
    outline = QuizletBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = QuizletBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2E3856),
    onPrimaryContainer = Color.White,
    secondary = QuizletYellow,
    onSecondary = QuizletNavy,
    secondaryContainer = Color(0xFF384364),
    onSecondaryContainer = Color.White,
    tertiary = QuizletGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF1E3A34),
    onTertiaryContainer = Color.White,
    background = QuizletBgDark,
    onBackground = Color.White,
    surface = QuizletSurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2B334D),
    onSurfaceVariant = Color(0xFFADB5CB),
    outline = Color(0xFF3B4668)
)

@Composable
fun VocabLearningAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}