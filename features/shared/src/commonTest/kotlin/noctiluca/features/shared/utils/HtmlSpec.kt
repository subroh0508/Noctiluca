package noctiluca.features.shared.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.should

class BuildAnnotatedHtmlStringFromHtmlSpec : DescribeSpec({
    data class TestData(
        val html: String,
        val expectedText: String,
        val expectedSpanStyles: List<AnnotatedString.Range<SpanStyle>>,
        val expectedParagraphStyles: List<AnnotatedString.Range<ParagraphStyle>>,
    ) : WithDataTestName {
        override fun dataTestName() = "$html -> $expectedText"
    }

    context("#buildAnnotatedStringFromHtml") {
        withData(
            TestData(
                "<p>吾輩は猫である。名前はまだない。</p>",
                "吾輩は猫である。名前はまだない。\n",
                emptyList(),
                emptyList(),
            ),
            TestData(
                "<p>一人の下人が、羅生門の下で雨やみを待っていた。<br>広い門の下には、この男の外に誰もいない。</p>",
                "一人の下人が、羅生門の下で雨やみを待っていた。\n広い門の下には、この男の外に誰もいない。\n",
                emptyList(),
                emptyList(),
            ),
        ) { (html, expectedTest, expectedSpanStyles, expectedParagraphStyles) ->
            buildAnnotatedStringFromHtml(
                html,
                Color.Blue,
                TextStyle.Default,
            ).apply {
                text should be(expectedTest)
                spanStyles should be(expectedSpanStyles)
                paragraphStyles should be(expectedParagraphStyles)
            }
        }
    }
})