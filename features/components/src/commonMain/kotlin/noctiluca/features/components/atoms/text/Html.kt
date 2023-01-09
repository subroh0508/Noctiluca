package noctiluca.features.components.atoms.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    if (text.contains("<span class=\"invisible\">")) {
        println(text)
    }

    ExpectHtmlText(removeInvisibleTags(text), modifier, style)
}

@Composable
internal expect fun ExpectHtmlText(
    text: String,
    modifier: Modifier,
    style: TextStyle,
)

private val REGEX_INVISIBLE_CLASS = """<[a-z]+ class="invisible">(.*?)</[a-z]+>""".toRegex()

private fun removeInvisibleTags(
    html: String,
): String {
    val matchResults = REGEX_INVISIBLE_CLASS.findAll(html).toList()

    return matchResults.foldIndexed(html) { index, acc, matchResult ->
        val ellipse =
            if (matchResults.size <= 1 || index < matchResults.lastIndex)
                ""
            else
                "..."

        acc.replace(matchResult.value, ellipse)
    }
}
