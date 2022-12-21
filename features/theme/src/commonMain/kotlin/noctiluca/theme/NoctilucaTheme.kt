package noctiluca.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import noctiluca.theme.blue.DarkBlue
import noctiluca.theme.blue.LightBlue
import noctiluca.theme.cyan.DarkCyan
import noctiluca.theme.cyan.LightCyan
import noctiluca.theme.purple.DarkPurple
import noctiluca.theme.purple.LightPurple
import noctiluca.theme.red.DarkRed
import noctiluca.theme.red.LightRed
import noctiluca.theme.yellow.DarkYellow
import noctiluca.theme.yellow.LightYellow

enum class ColorPalette { BLUE, CYAN, RED, PURPLE, YELLOW }

@Composable
fun NoctilucaTheme(
    palette: ColorPalette = ColorPalette.BLUE,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (palette) {
        ColorPalette.BLUE -> if (useDarkTheme) DarkBlue else LightBlue
        ColorPalette.CYAN -> if (useDarkTheme) DarkCyan else LightCyan
        ColorPalette.RED -> if (useDarkTheme) DarkRed else LightRed
        ColorPalette.PURPLE -> if (useDarkTheme) DarkPurple else LightPurple
        ColorPalette.YELLOW -> if (useDarkTheme) DarkYellow else LightYellow
    }

    MaterialTheme(colorScheme, content = content)
}
