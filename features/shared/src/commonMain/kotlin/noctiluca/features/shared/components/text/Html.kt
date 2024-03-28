package noctiluca.features.shared.components.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import noctiluca.features.shared.utils.buildAnnotatedStringFromHtml

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    urlColor: Color = MaterialTheme.colorScheme.primary,
    color: Color = Color.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = LocalTextStyle.current,
) = Text(
    buildAnnotatedStringFromHtml(text, urlColor, style),
    modifier = modifier,
    color = color,
    overflow = overflow,
    style = style,
)
