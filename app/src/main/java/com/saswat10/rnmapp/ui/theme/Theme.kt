package com.saswat10.rnmapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DraculaCurrentLine,
    secondary = DraculaPurple,
    tertiary = DraculaPink,
    background = DraculaBackground,
    surface = DraculaCurrentLine,
    onPrimary = DraculaForeground,
    onSecondary = DraculaForeground,
    onTertiary = DraculaForeground,
    onBackground = DraculaForeground,
    onSurface = DraculaForeground,
    error = DraculaRed
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    /* Other default colors to override

    */
)

@Composable
fun RnMAppTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val navigationBarDark = Color.Transparent.toArgb()

    val isDarkMode = isSystemInDarkTheme()
    val context = LocalContext.current as ComponentActivity

    DisposableEffect(isDarkMode) {
        context.enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(navigationBarDark),
            statusBarStyle = SystemBarStyle.dark(navigationBarDark)
        )
        onDispose { }
    }

    val colorScheme = when {
        darkTheme -> darkColorScheme()
        else -> darkColorScheme()
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
