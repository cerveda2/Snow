package cz.dcervenka.snow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    primaryContainer = GreenPrimaryDark,
    secondary = YellowAccent,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    error = ErrorRed,
    onError = Color.Black,

    secondaryContainer = SnowInfoBlue,
    tertiary = TracksOpenGreen,
    tertiaryContainer = SuccessGreen,
    outline = DividerGray
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    primaryContainer = GreenPrimaryDark,
    secondary = YellowAccent,
    background = BackgroundWhite,
    surface = BackgroundWhite,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorRed,
    onError = Color.White,

    secondaryContainer = SnowInfoBlue,
    tertiary = TracksOpenGreen,
    tertiaryContainer = SuccessGreen,
    outline = DividerGray

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val LightColorSchemeV2 = lightColorScheme(
    primary = LightPrimary,
    primaryContainer = LightPrimaryContainer,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnSecondary,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface
)

private val DarkColorSchemeV2 = darkColorScheme(
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface
)

@Composable
fun SnowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorSchemeV2
        else -> LightColorSchemeV2
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}