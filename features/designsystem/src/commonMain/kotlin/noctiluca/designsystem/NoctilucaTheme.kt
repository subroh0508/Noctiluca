package noctiluca.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import noctiluca.designsystem.blue.DarkBlue
import noctiluca.designsystem.blue.LightBlue
import noctiluca.designsystem.cyan.DarkCyan
import noctiluca.designsystem.cyan.LightCyan
import noctiluca.designsystem.purple.DarkPurple
import noctiluca.designsystem.purple.LightPurple
import noctiluca.designsystem.red.DarkRed
import noctiluca.designsystem.red.LightRed
import noctiluca.designsystem.yellow.DarkYellow
import noctiluca.designsystem.yellow.LightYellow

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
