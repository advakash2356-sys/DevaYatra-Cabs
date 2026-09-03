package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Saffron,
    onPrimary = DeepShivBlue,
    primaryContainer = BloodRed,
    onPrimaryContainer = Color.White,
    secondary = ShivBlue,
    onSecondary = Color.White,
    tertiary = Marigold,
    background = DeepShivBlue, // Ultra dark cosmic blue
    surface = Color(0xFF1E293B), // Elevated surface
    onBackground = BhasmGray,
    onSurface = BhasmGray,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = BhasmGray,
    error = AlertRed
)

private val LightColorScheme = lightColorScheme(
    primary = Saffron,
    onPrimary = Color.White,
    primaryContainer = BloodRed,
    onPrimaryContainer = Color.White,
    secondary = ShivBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDBEAFE),
    onSecondaryContainer = ShivBlue,
    tertiary = Marigold,
    background = Color(0xFFFFF7ED), // Warm, slightly saffron tinted bg
    surface = CardLight,
    onBackground = DeepShivBlue,
    onSurface = DeepShivBlue,
    surfaceVariant = Color(0xFFFFEDD5),
    onSurfaceVariant = RudrakshaBrown,
    error = AlertRed
)

@Composable
fun DevaYatraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic colors for a strong brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
