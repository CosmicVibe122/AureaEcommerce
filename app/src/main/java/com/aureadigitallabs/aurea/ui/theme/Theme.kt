package com.aurea.app.ui.theme



import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AureaGold,
    secondary = AureaAccent,
    background = AureaBlack,
    surface = AureaGray,
    onPrimary = AureaBlack,
    onSecondary = AureaBlack,
    onBackground = AureaWhite,
    onSurface = AureaWhite,
    error = AureaError,
    onError = AureaWhite
)

private val LightColorScheme = lightColorScheme(
    primary = AureaGold,
    secondary = AureaAccent,
    background = AureaWhite,
    surface = AureaWhite,
    onPrimary = AureaBlack,
    onSecondary = AureaBlack,
    onBackground = AureaBlack,
    onSurface = AureaBlack,
    error = AureaError,
    onError = AureaWhite
)

@Composable
fun AureaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
