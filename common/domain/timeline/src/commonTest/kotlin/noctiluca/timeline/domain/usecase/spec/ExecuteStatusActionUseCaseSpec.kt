package noctiluca.timeline.domain.usecase.spec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import noctiluca.timeline.domain.TestTimelineUseCaseComponent
import noctiluca.timeline.domain.usecase.ExecuteStatusActionUseCase

class ExecuteStatusActionUseCaseSpec : DescribeSpec({
    val component = TestTimelineUseCaseComponent(
        MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"ip":"127.0.0.1"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
    )

    val useCase: ExecuteStatusActionUseCase = component.scope.get()

    describe("#test") {
        it("should be correctly") {
            1 + 2 should be(3)
        }
    }
})
