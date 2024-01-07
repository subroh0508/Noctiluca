package noctiluca.authentication.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.containExactlyInAnyOrder
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import noctiluca.authentication.domain.TestAuthenticationUseCaseComponent
import noctiluca.authentication.domain.mock.MockAppCredentialDataStore
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.json.*
import noctiluca.model.HttpException
import noctiluca.model.authentication.Instance
import noctiluca.network.mastodon.Api
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.mock.buildEmptyMockAuthenticationTokenDataStore
import java.net.UnknownHostException
import noctiluca.network.instancessocial.Api as InstancesSocialApi

class SearchMastodonInstancesUseCaseSpec : DescribeSpec({
    describe("#execute: for v4") {
        context("when the server returns valid response") {
            val useCase = buildUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Instance(), JSON_V4_INSTANCE_BY_V1_API)
                    .mock(Api.V2.Instance(), JSON_V4_INSTANCE_BY_V2_API)
                    .mock(Api.V1.Instance.ExtendedDescription(), JSON_EXTENDED_DESCRIPTION)
                    .build(),
            )

            it("returns instance information for suggestion") {
                runBlocking { useCase.execute(DOMAIN_SAMPLE_COM) }.also {
                    it should haveSize(1)
                    it.first().domain should be(DOMAIN_SAMPLE_COM)
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

        val query = "sample"

        context("when the client raises UnknownHostException") {
            context("and the instances.social api returns valid response") {
                val useCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), UnknownHostException())
                        .mock(InstancesSocialApi.Instances.Search(), JSON_INSTANCES)
                        .build(),
                )

                it("returns instances information for suggestion") {
                    runBlocking { useCase.execute(query) }.also {
                        it should haveSize(4)
                        it.map(Instance.Suggest::domain) should containExactlyInAnyOrder(
                            DOMAIN_SAMPLE_COM,
                            INSTANCE_DOMAIN_SUGGESTION_1,
                            INSTANCE_DOMAIN_SUGGESTION_2,
                            V3_INSTANCE_NAME,
                        )
                    }
                }
            }

            context("and the instances.social api returns invalid response") {
                val useCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), UnknownHostException())
                        .mock(InstancesSocialApi.Instances.Search(), HttpStatusCode.BadRequest)
                        .build(),
                )

                it("raises HttpException") {
                    shouldThrowExactly<HttpException> {
                        runBlocking { useCase.execute(query) }
                    }
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

            it("returns instance information for suggestion") {
                runBlocking { useCase.execute(DOMAIN_V3_INSTANCE) }.also {
                    it should haveSize(1)
                    it.first().domain should be(DOMAIN_V3_INSTANCE)
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

        val query = "sample"

        context("when the client raises UnknownHostException") {
            context("and the instances.social api returns valid response") {
                val useCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), UnknownHostException())
                        .mock(InstancesSocialApi.Instances.Search(), JSON_INSTANCES)
                        .build(),
                )

                it("returns instances information for suggestion") {
                    runBlocking { useCase.execute(query) }.also {
                        it should haveSize(4)
                        it.map(Instance.Suggest::domain) should containExactlyInAnyOrder(
                            DOMAIN_SAMPLE_COM,
                            INSTANCE_DOMAIN_SUGGESTION_1,
                            INSTANCE_DOMAIN_SUGGESTION_2,
                            V3_INSTANCE_NAME,
                        )
                    }
                }
            }

            context("and the instances.social api returns invalid response") {
                val useCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), UnknownHostException())
                        .mock(InstancesSocialApi.Instances.Search(), HttpStatusCode.BadRequest)
                        .build(),
                )

                it("raises HttpException") {
                    shouldThrowExactly<HttpException> {
                        runBlocking { useCase.execute(query) }
                    }
                }
            }
        }
    }
})

private fun buildUseCase(
    engine: MockEngine,
): SearchMastodonInstancesUseCase = TestAuthenticationUseCaseComponent(
    engine,
    MockAppCredentialDataStore(),
    buildEmptyMockAuthenticationTokenDataStore(),
).scope.get()
