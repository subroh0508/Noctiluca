package noctiluca.features.shared.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
internal actual fun ExpectHtmlText(
    text: String,
    modifier: Modifier,
    urlColor: Color,
    color: Color,
    overflow: TextOverflow,
    style: TextStyle,
) {
    Text(
        text,
        modifier,
        style = style,
    )
}
