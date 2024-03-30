package noctiluca.features.shared.spec.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.shared.ENCODED_HTML_PARAGRAPH
import noctiluca.features.shared.ENCODED_TEXT_PARAGRAPH
import noctiluca.features.shared.SIMPLE_HTML_PARAGRAPH_1
import noctiluca.features.shared.SIMPLE_HTML_PARAGRAPH_2
import noctiluca.features.shared.SIMPLE_TEXT_PARAGRAPH_1
import noctiluca.features.shared.SIMPLE_TEXT_PARAGRAPH_2
import noctiluca.features.shared.WITH_HASH_TAG_HTML_PARAGRAPH
import noctiluca.features.shared.WITH_HASH_TAG_TEXT_PARAGRAPH
import noctiluca.features.shared.WITH_LINK_HTML_PARAGRAPH
import noctiluca.features.shared.WITH_LINK_TEXT_PARAGRAPH
import noctiluca.features.shared.utils.buildAnnotatedStringFromHtml

class BuildAnnotatedHtmlStringFromHtmlSpec : DescribeSpec({
    data class TestData(
        val html: String,
        val expectedText: String,
        val expectedSpanStyles: List<AnnotatedString.Range<SpanStyle>>,
        val expectedParagraphStyles: List<AnnotatedString.Range<ParagraphStyle>>,
    ) : WithDataTestName {
        override fun dataTestName() = "$html -> $expectedText"
    }

    val urlSpanStyle = SpanStyle(color = Color.Blue)

    context("#buildAnnotatedStringFromHtml") {
        withData(
            TestData(
                SIMPLE_HTML_PARAGRAPH_1,
                SIMPLE_TEXT_PARAGRAPH_1,
                emptyList(),
                emptyList(),
            ),
            TestData(
                SIMPLE_HTML_PARAGRAPH_2,
                SIMPLE_TEXT_PARAGRAPH_2,
                emptyList(),
                emptyList(),
            ),
            TestData(
                WITH_LINK_HTML_PARAGRAPH,
                WITH_LINK_TEXT_PARAGRAPH,
                listOf(
                    AnnotatedString.Range(urlSpanStyle, 18, 18),
                    AnnotatedString.Range(urlSpanStyle, 18, 51),
                    AnnotatedString.Range(urlSpanStyle, 51, 51),
                ),
                emptyList(),
            ),
            TestData(
                WITH_HASH_TAG_HTML_PARAGRAPH,
                WITH_HASH_TAG_TEXT_PARAGRAPH,
                listOf(
                    AnnotatedString.Range(urlSpanStyle, 20, 21),
                    AnnotatedString.Range(urlSpanStyle, 21, 26),
                ),
                emptyList(),
            ),
            TestData(
                ENCODED_HTML_PARAGRAPH,
                ENCODED_TEXT_PARAGRAPH,
                emptyList(),
                emptyList(),
            ),
        ) { (html, expectedTest, expectedSpanStyles, expectedParagraphStyles) ->
            buildAnnotatedStringFromHtml(
                html,
                urlSpanStyle.color,
                TextStyle.Default,
            ).apply {
                text should be(expectedTest)
                spanStyles should be(expectedSpanStyles)
                paragraphStyles should be(expectedParagraphStyles)
            }
        }
    }
})
