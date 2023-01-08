package noctiluca.components.atoms.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) = ExpectHtmlText(text, modifier, style)

@Composable
internal expect fun ExpectHtmlText(
    text: String,
    modifier: Modifier,
    style: TextStyle,
)
