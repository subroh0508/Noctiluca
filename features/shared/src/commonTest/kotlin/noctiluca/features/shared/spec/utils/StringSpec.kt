package noctiluca.features.shared.spec.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.shared.utils.format

class FormatSpec : DescribeSpec({
    context("#format") {
        it("returns formatted string") {
            "@%sさんをミュート".format("subroh_0508") should be("@subroh_0508さんをミュート")
        }

        it("returns formatted string (integer)") {
            "%d秒前".format(10) should be("10秒前")
        }

        it("returns formatted string (integer and comma)") {
            "%,d人".format(10000) should be("10,000人")
        }
    }
})
