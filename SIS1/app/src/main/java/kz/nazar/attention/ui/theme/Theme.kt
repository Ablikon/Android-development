package kz.nazar.attention.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = Focus,
    secondary = Calm,
    tertiary = Drift,
    background = Ink,
    surface = InkSoft,
)

private val LightColors = lightColorScheme(
    primary = Focus,
    secondary = Calm,
    tertiary = Drift,
)

@Composable
fun NazarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content,
    )
}
