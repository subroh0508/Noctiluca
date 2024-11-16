package noctiluca.features.shared.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import com.mohamedrejeb.ksoup.entities.KsoupEntities.decodeHtml
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

const val ANCHOR_TAG = "ANCHOR_TAG"

fun buildAnnotatedStringFromHtml(
    text: String,
    urlColor: Color,
    textStyle: TextStyle,
) = getAnnotatedStringBuilder(
    text,
    textStyle,
    SpanStyle(urlColor),
).toAnnotatedString()

private fun getAnnotatedStringBuilder(
    text: String,
    textStyle: TextStyle,
    urlSpanStyle: SpanStyle,
): AnnotatedString.Builder {
    val builder = AnnotatedString.Builder()

    val unclosedTags = mutableListOf<String>()
    val foldedAttributes = mutableMapOf<String, String>()
    var attributes = mapOf<String, String>()

    val htmlHandler = KsoupHtmlHandler.Builder()
        .onOpenTag { name, attr, _ ->
            unclosedTags.add(name)
            foldedAttributes += attr
            attributes = attr
        }
        .onText {
            builder.append(
                it,
                unclosedTags,
                attributes,
                textStyle,
                urlSpanStyle,
            )
        }
        .onCloseTag { _, _ ->
            builder.append(unclosedTags)

            // @see: https://developer.android.com/about/versions/15/behavior-changes-15#openjdk-api-changes
            unclosedTags.removeAt(unclosedTags.lastIndex)
            attributes.keys.map { foldedAttributes.remove(it) }
            attributes = mapOf()
        }
        .build()

    with(KsoupHtmlParser(htmlHandler)) {
        write(text)
        end()
    }

    return builder
}

private fun AnnotatedString.Builder.append(
    text: String,
    unclosedTags: List<String>,
    attributes: Map<String, String>,
    textStyle: TextStyle,
    urlSpanStyle: SpanStyle,
) = withStyle(
    unclosedTags,
    attributes,
    textStyle,
    urlSpanStyle,
) { append(decodeHtml(text)) }

private fun AnnotatedString.Builder.append(
    unclosedTags: List<String>,
) {
    when (unclosedTags.last()) {
        "p", "br" -> append("\n")
    }
}

private fun AnnotatedString.Builder.withStyle(
    unclosedTags: List<String>,
    attributes: Map<String, String>,
    textStyle: TextStyle,
    urlSpanStyle: SpanStyle,
    block: AnnotatedString.Builder.() -> Unit,
) {
    if (unclosedTags.isEmpty()) {
        handleClassName(attributes, fallback = block)
        return
    }

    val tag = unclosedTags.last()
    val next = {
        withStyle(
            unclosedTags - tag,
            attributes,
            textStyle,
            urlSpanStyle,
            block,
        )
    }

    when (tag) {
        "a" -> withUrlStyle(attributes, urlSpanStyle) { next() }
        "b", "strong" -> withBoldStyle(textStyle) { next() }
        "i", "em" -> withItalicStyle(textStyle) { next() }
        "u", "ins" -> withUnderlineStyle(textStyle) { next() }
        "s", "del" -> withStrikethrough(textStyle) { next() }
        else -> next()
    }
}

private fun AnnotatedString.Builder.handleClassName(
    attributes: Map<String, String>,
    fallback: AnnotatedString.Builder.() -> Unit,
) {
    val className = attributes["class"] ?: ""

    when {
        className.contains("invisible") -> Unit
        className.contains("ellipsis") -> {
            fallback()
            append("...")
        }
        else -> fallback()
    }
}

@OptIn(ExperimentalTextApi::class)
private fun AnnotatedString.Builder.withUrlStyle(
    attributes: Map<String, String>,
    urlSpanStyle: SpanStyle,
    block: AnnotatedString.Builder.() -> Unit,
) = withAnnotation(
    tag = ANCHOR_TAG,
    annotation = attributes["href"] ?: "",
) {
    withStyle(urlSpanStyle, block)
}

private fun AnnotatedString.Builder.withBoldStyle(
    textStyle: TextStyle,
    block: AnnotatedString.Builder.() -> Unit,
) = withStyle(textStyle.copy(fontWeight = FontWeight.Bold).toSpanStyle(), block)

private fun AnnotatedString.Builder.withItalicStyle(
    textStyle: TextStyle,
    block: AnnotatedString.Builder.() -> Unit,
) = withStyle(textStyle.copy(fontStyle = FontStyle.Italic).toSpanStyle(), block)

private fun AnnotatedString.Builder.withUnderlineStyle(
    textStyle: TextStyle,
    block: AnnotatedString.Builder.() -> Unit,
) = withStyle(textStyle.copy(textDecoration = TextDecoration.Underline).toSpanStyle(), block)

private fun AnnotatedString.Builder.withStrikethrough(
    textStyle: TextStyle,
    block: AnnotatedString.Builder.() -> Unit,
) = withStyle(textStyle.copy(textDecoration = TextDecoration.LineThrough).toSpanStyle(), block)
