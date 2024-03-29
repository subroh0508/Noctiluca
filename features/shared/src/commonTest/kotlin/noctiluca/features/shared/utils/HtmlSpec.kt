package noctiluca.features.shared.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.should

class BuildAnnotatedHtmlStringFromHtmlSpec : DescribeSpec({
    describe("#buildAnnotatedStringFromHtml") {
        it("should return AnnotatedString from html") {
            buildAnnotatedStringFromHtml(
                "<p>吾輩は猫である。名前はまだない。</p>",
                Color.Blue,
                TextStyle.Default,
            ).apply {
                text should be("吾輩は猫である。名前はまだない。\n")
                spanStyles should beEmpty()
                paragraphStyles should beEmpty()
            }
        }
    }
})