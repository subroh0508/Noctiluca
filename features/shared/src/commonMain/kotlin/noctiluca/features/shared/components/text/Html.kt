package noctiluca.features.shared.components.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    urlColor: Color = MaterialTheme.colorScheme.primary,
    color: Color = Color.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = LocalTextStyle.current,
) = ExpectHtmlText(
    removeInvisibleTags(text),
    modifier,
    urlColor,
    color,
    overflow,
    style,
)

@Composable
internal expect fun ExpectHtmlText(
    text: String,
    modifier: Modifier,
    urlColor: Color,
    color: Color,
    overflow: TextOverflow,
    style: TextStyle,
)

private val REGEX_INVISIBLE_CLASS = """<[a-z]+ class="invisible">(.*?)</[a-z]+>""".toRegex()
private val REGEX_ELLIPSIS_CLASS = """(<[a-z]+ class="ellipsis">)(.*?)(</[a-z]+>)""".toRegex()

@Suppress("MagicNumber")
private fun removeInvisibleTags(
    html: String,
): String {
    val label = REGEX_INVISIBLE_CLASS.findAll(html)
        .toList()
        .fold(html) { acc, matchResult ->
            acc.replace(matchResult.value, "")
        }

    return REGEX_ELLIPSIS_CLASS.findAll(label)
        .fold(label) { acc, matchResult ->
            val prefix = matchResult.groupValues[1]
            val postfix = matchResult.groupValues[3]

            acc.replace(matchResult.value, prefix + matchResult.groupValues[2] + "..." + postfix)
        }
}
