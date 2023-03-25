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
import noctiluca.api.instancessocial.Api as InstancesSocialApi
import noctiluca.api.mastodon.Api
import noctiluca.authentication.domain.TestAuthenticationUseCaseComponent
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.json.*
import noctiluca.instance.model.Instance
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.mock.MockHttpClientEngine
import java.net.UnknownHostException

class SearchMastodonInstancesUseCaseSpec : DescribeSpec({
    describe("#execute") {
        context("when the server returns valid response") {
            val useCase = buildUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Instance(), JSON_INSTANCE)
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
                    .build(),
            )

            it("raises ClientRequestException") {
                shouldThrowExactly<ClientRequestException> {
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
                        .mock(InstancesSocialApi.Instances(), JSON_INSTANCES)
                        .build(),
                )

                it("returns instances information for suggestion") {
                    runBlocking { useCase.execute(query) }.also {
                        it should haveSize(4)
                        it.map(Instance.Suggest::domain) should containExactlyInAnyOrder(
                            DOMAIN_SAMPLE_COM,
                            INSTANCE_DOMAIN_SUGGESTION_1,
                            INSTANCE_DOMAIN_SUGGESTION_2,
                            INSTANCE_DOMAIN_SUGGESTION_3,
                        )
                    }
                }
            }

            context("and the instances.social api returns invalid response") {
                val useCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), UnknownHostException())
                        .mock(InstancesSocialApi.Instances(), HttpStatusCode.BadRequest)
                        .build(),
                )

                it("raises ClientRequestException") {
                    shouldThrowExactly<ClientRequestException> {
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
).scope.get()
