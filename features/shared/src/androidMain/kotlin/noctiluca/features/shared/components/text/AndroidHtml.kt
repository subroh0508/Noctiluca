package noctiluca.features.shared.components.text

import android.graphics.Typeface
import android.text.Html
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.getSpans
import androidx.compose.ui.text.buildAnnotatedString as buildComposeAnnotatedString

@Composable
internal actual fun ExpectHtmlText(
    text: String,
    modifier: Modifier,
    urlColor: Color,
    color: Color,
    overflow: TextOverflow,
    style: TextStyle,
) {
    val spanned = remember(text) { Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT) }

    val urlSpanStyle = SpanStyle(
        color = urlColor,
        textDecoration = TextDecoration.Underline,
    )

    Text(
        buildAnnotatedString(spanned, style, urlSpanStyle),
        modifier = modifier,
        color = color,
        overflow = overflow,
        style = style,
    )
}

private fun buildAnnotatedString(
    spanned: Spanned,
    textStyle: TextStyle,
    urlSpanStyle: SpanStyle,
) = buildComposeAnnotatedString {
    append(spanned.toString())
    // addStyle(textStyle.toSpanStyle(), 0, spanned.length)

    with(spanned) {
        forSpansEach<URLSpan> { start, end, urlSpan ->
            addStyle(urlSpanStyle, start, end)
            addStringAnnotation("url", urlSpan.url, start, end)
        }

        forSpansEach<StyleSpan> { start, end, styleSpan ->
            val style = when (styleSpan.style) {
                Typeface.BOLD -> textStyle.copy(fontWeight = FontWeight.Bold)
                Typeface.ITALIC -> textStyle.copy(fontStyle = FontStyle.Italic)
                Typeface.BOLD_ITALIC -> textStyle.copy(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                else -> textStyle
            }

            addStyle(style.toSpanStyle(), start, end)
        }

        forSpansEach<ForegroundColorSpan> { start, end, colorSpan ->
            addStyle(textStyle.copy(color = Color(colorSpan.foregroundColor)).toSpanStyle(), start, end)
        }

        forSpansEach<UnderlineSpan> { start, end, _ ->
            addStyle(textStyle.copy(textDecoration = TextDecoration.Underline).toSpanStyle(), start, end)
        }

        forSpansEach<StrikethroughSpan> { start, end, _ ->
            addStyle(textStyle.copy(textDecoration = TextDecoration.LineThrough).toSpanStyle(), start, end)
        }
    }
}

private inline fun <reified T : Any> Spanned.forSpansEach(
    start: Int = 0,
    end: Int = length,
    block: (Int, Int, T) -> Unit,
) = getSpans<T>(start, end).forEach {
    block(getSpanStart(it), getSpanEnd(it), it)
}
