package noctiluca.features.shared.spec.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.shared.utils.format

class FormatSpec : DescribeSpec({
    context("#format") {
        it("returns formatted string") {
            "%d秒前".format(10) should be("10秒前")
            "%,d人".format(10000) should be("10,000人")
            "@%sさんをミュート".format("subroh_0508") should be("@subroh_0508さんをミュート")
        }
    }
})
