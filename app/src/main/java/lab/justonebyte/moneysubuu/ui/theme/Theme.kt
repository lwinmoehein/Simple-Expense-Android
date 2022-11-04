package lab.justonebyte.moneysubuu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = primary,
    primaryVariant = primaryVariant,
    onPrimary = Color.White,
    secondary = complementary,
    secondaryVariant = complementary,
    onSecondary = Color.White,
    error = Red800
)

private val DarkThemeColors = darkColors(
    primary = primaryLight,
    primaryVariant = primaryVariant,
    onPrimary = Color.Black,
    secondary = complementaryLight,
    onSecondary = Color.Black,
    error = Red200
)

@Composable
fun MoneySuBuuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = SuBuuTypography,
        shapes = SuBuuShapes,
        content = content
    )
}