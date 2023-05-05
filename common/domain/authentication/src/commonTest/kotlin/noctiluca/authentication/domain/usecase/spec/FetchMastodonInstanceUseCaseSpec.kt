package noctiluca.authentication.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import noctiluca.api.mastodon.Api
import noctiluca.authentication.domain.TestAuthenticationUseCaseComponent
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.authentication.domain.usecase.json.JSON_INSTANCE
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.mock.MockHttpClientEngine

class FetchMastodonInstanceUseCaseSpec : DescribeSpec({
    describe("#execute") {
        context("when the server returns valid response") {
            val useCase = buildUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Instance(), JSON_INSTANCE)
                    .build(),
            )

            it("returns detail of instance") {
                runBlocking { useCase.execute(DOMAIN_SAMPLE_COM) }.also {
                    it.domain should be(DOMAIN_SAMPLE_COM)
                }
            }
        }

        context("when the server returns invalid response") {
            val useCase = buildUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Instance(), HttpStatusCode.BadRequest)
                    .build(),
            )

            it("raises ClientRequestException") {
                shouldThrowExactly<ClientRequestException> {
                    runBlocking { useCase.execute(DOMAIN_SAMPLE_COM) }
                }
            }
        }
    }
})

private fun buildUseCase(
    engine: MockEngine,
): FetchMastodonInstanceUseCase = TestAuthenticationUseCaseComponent(
    engine,
).scope.get()
