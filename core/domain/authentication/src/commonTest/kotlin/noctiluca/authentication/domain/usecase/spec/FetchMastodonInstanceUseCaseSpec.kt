package noctiluca.authentication.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import noctiluca.authentication.domain.TestAuthenticationUseCaseComponent
import noctiluca.authentication.domain.mock.MockAppCredentialDataStore
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.authentication.domain.usecase.json.*
import noctiluca.model.HttpException
import noctiluca.network.mastodon.Api
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.mock.MockAuthenticationTokenDataStore
import noctiluca.test.mock.MockHttpClientEngine

class FetchMastodonInstanceUseCaseSpec : DescribeSpec({
    describe("#execute: for v4") {
        context("when the server returns valid response") {
            val useCase = buildUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Instance(), JSON_V4_INSTANCE_BY_V1_API)
                    .mock(Api.V2.Instance(), JSON_V4_INSTANCE_BY_V2_API)
                    .mock(Api.V1.Instance.ExtendedDescription(), JSON_EXTENDED_DESCRIPTION)
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
                    .mock(Api.V1.Instance.ExtendedDescription(), HttpStatusCode.BadRequest)
                    .build(),
            )

            it("raises HttpException") {
                shouldThrowExactly<HttpException> {
                    runBlocking { useCase.execute(DOMAIN_SAMPLE_COM) }
                }
            }
        }
    }

    describe("#execute: for v3") {
        context("when the server returns valid response") {
            val useCase = buildUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Instance(), JSON_V3_INSTANCE_BY_V1_API)
                    .build(),
            )

            it("returns detail of instance") {
                runBlocking { useCase.execute(DOMAIN_V3_INSTANCE) }.also {
                    it.domain should be(DOMAIN_V3_INSTANCE)
                }
            }
        }

        context("when the server returns invalid response") {
            val useCase = buildUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Instance(), HttpStatusCode.BadRequest)
                    .mock(Api.V1.Instance.ExtendedDescription(), HttpStatusCode.NotFound)
                    .build(),
            )

            it("raises HttpException") {
                shouldThrowExactly<HttpException> {
                    runBlocking { useCase.execute(DOMAIN_V3_INSTANCE) }
                }
            }
        }
    }
})

private fun buildUseCase(
    engine: MockEngine,
): FetchMastodonInstanceUseCase = TestAuthenticationUseCaseComponent(
    engine,
    MockAppCredentialDataStore(),
    MockAuthenticationTokenDataStore(),
).scope.get()
