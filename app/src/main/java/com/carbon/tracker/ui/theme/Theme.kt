package com.carbon.tracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = WhiteColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = DarkBackgroundColor,
    onBackground = WhiteColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = WhiteColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = WhiteColor,
    onBackground = DarkBackgroundColor
)

@Composable
fun CarbonTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalSpacing provides Dimension()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}