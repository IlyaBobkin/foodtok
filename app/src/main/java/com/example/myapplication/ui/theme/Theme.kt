package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FoodTokDark = darkColorScheme(
    primary = Peach,
    secondary = Aqua,
    tertiary = Lime,
    background = Night,
    surface = NightSoft,
    onPrimary = TextPrimary,
    onSecondary = Night,
    onTertiary = Night,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun FoodTokTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FoodTokDark,
        typography = Typography,
        content = content
    )
}
