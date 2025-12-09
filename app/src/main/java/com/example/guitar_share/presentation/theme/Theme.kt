package com.example.guitar_share.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = WhiteText,

    background = BlackBackground,
    onBackground = WhiteText,

    surfaceVariant = DarkSurface,
    surface = DarkSurface,
    onSurface = WhiteText,

    error = ErrorRed
)

// (Opcional) Aquí definirías el LightColorScheme para el futuro
private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun GuitarShareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta el modo del sistema
    dynamicColor: Boolean = false,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Barra de estado negra
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de tener Typography.kt o bórralo si no usas tipografía custom
        content = content
    )
}