package noctiluca.data.spec.authorization

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.collections.matchInOrder
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import noctiluca.data.TestDataComponent
import noctiluca.data.instance.InstanceRepository
import noctiluca.data.instance.impl.InstanceRepositoryImpl
import noctiluca.data.json.authorization.*
import noctiluca.data.spec.accountdetail.prevStatus
import noctiluca.data.spec.accountdetail.status
import noctiluca.model.HttpException
import noctiluca.model.Uri
import noctiluca.model.authorization.Instance
import noctiluca.network.authorization.di.AuthorizationApiModule
import noctiluca.network.instancessocial.di.InstancesSocialApiModule
import noctiluca.network.instancessocial.di.buildHttpClient
import noctiluca.network.mastodon.Api
import noctiluca.test.shared.DOMAIN_SAMPLE_COM
import noctiluca.test.shared.JSON_STATUS_NORMAL
import noctiluca.test.shared.JSON_STATUS_NORMAL_ID_99
import noctiluca.test.shared.MockUnknownHostException
import noctiluca.test.shared.URL_SAMPLE_COM
import noctiluca.test.shared.extension.flowToList
import noctiluca.test.shared.mock.MockHttpClientEngine
import noctiluca.test.shared.mock.buildEmptyMockAuthenticationTokenDataStore
import org.koin.core.component.get
import noctiluca.network.instancessocial.Api as InstancesSocialApi

class InstanceRepositorySpec : DescribeSpec({
    coroutineTestScope = true

    val v4Suggest = Instance.Suggest(
        DOMAIN_SAMPLE_COM,
        "description",
        Uri("$URL_SAMPLE_COM/files/thumbnail.png"),
        Instance.Version(4, 0, 2),
    )

    val v3Suggest = Instance.Suggest(
        DOMAIN_V3_INSTANCE,
        "short description",
        Uri("$URL_V3_INSTANCE/files/thumbnail.png"),
        Instance.Version(3, 3, 3),
    )

    describe("#search") {
        context("for v4") {
            context("when the server returns valid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), JSON_V4_INSTANCE_BY_V1_API)
                        .mock(Api.V2.Instance(), JSON_V4_INSTANCE_BY_V2_API)
                        .mock(Api.V1.Instance.ExtendedDescription(), JSON_EXTENDED_DESCRIPTION)
                        .build(),
                )

                it("returns instance information for suggestion") {
                    val suggests = flowToList(repository.suggests())

                    runBlocking {
                        repository.search(DOMAIN_SAMPLE_COM)

                        suggests should haveSize(2)
                        suggests should matchInOrder(
                            { it should beEmpty() },
                            { it should containExactly(v4Suggest) },
                        )
                    }
                }
            }

            context("when the server returns invalid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), HttpStatusCode.BadRequest)
                        .mock(Api.V1.Instance.ExtendedDescription(), HttpStatusCode.BadRequest)
                        .build(),
                )

                it("raises HttpException") {
                    val suggests = flowToList(repository.suggests())

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.search(DOMAIN_SAMPLE_COM)
                        }

                        suggests should haveSize(1)
                        suggests should matchInOrder(
                            { it should beEmpty() },
                        )
                    }
                }
            }

            val query = "sample"

            context("when the client raises UnknownHostException") {
                context("and the instances.social api returns valid response") {
                    val repository = buildRepository(
                        MockHttpClientEngine
                            .mock(Api.V1.Instance(), MockUnknownHostException)
                            .mock(InstancesSocialApi.Instances.Search(), JSON_INSTANCES)
                            .build(),
                    )

                    it("returns instances information for suggestion") {
                        val suggests = flowToList(repository.suggests())

                        runBlocking {
                            repository.search(query)

                            suggests should haveSize(2)
                            suggests should matchInOrder(
                                { it should beEmpty() },
                                {
                                    it.map(Instance.Suggest::domain) should containExactly(
                                        DOMAIN_SAMPLE_COM,
                                        INSTANCE_DOMAIN_SUGGESTION_1,
                                        INSTANCE_DOMAIN_SUGGESTION_2,
                                        V3_INSTANCE_NAME,
                                    )
                                },
                            )
                        }
                    }
                }

                context("and the instances.social api returns invalid response") {
                    val repository = buildRepository(
                        MockHttpClientEngine
                            .mock(Api.V1.Instance(), MockUnknownHostException)
                            .mock(InstancesSocialApi.Instances.Search(), HttpStatusCode.BadRequest)
                            .build(),
                    )

                    it("raises HttpException") {
                        val suggests = flowToList(repository.suggests())

                        runBlocking {
                            shouldThrowExactly<HttpException> {
                                repository.search(query)
                            }

                            suggests should haveSize(1)
                            suggests should matchInOrder(
                                { it should beEmpty() },
                            )
                        }
                    }
                }
            }
        }

        context("for v3") {
            context("when the server returns valid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), JSON_V3_INSTANCE_BY_V1_API)
                        .build(),
                )

                it("returns instance information for suggestion") {
                    val suggests = flowToList(repository.suggests())

                    runBlocking {
                        repository.search(DOMAIN_V3_INSTANCE)

                        suggests should haveSize(2)
                        suggests should matchInOrder(
                            { it should beEmpty() },
                            { it should containExactly(v3Suggest) },
                        )
                    }
                }
            }

            context("when the server returns invalid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), HttpStatusCode.BadRequest)
                        .mock(Api.V1.Instance.ExtendedDescription(), HttpStatusCode.NotFound)
                        .build(),
                )

                it("raises HttpException") {
                    val suggests = flowToList(repository.suggests())

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.search(DOMAIN_V3_INSTANCE)
                        }

                        suggests should haveSize(1)
                        suggests should matchInOrder(
                            { it should beEmpty() },
                        )
                    }
                }
            }
        }
    }

    describe("#fetchInstance") {
        context("for v4") {
            context("when the server returns valid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), JSON_V4_INSTANCE_BY_V1_API)
                        .mock(Api.V2.Instance(), JSON_V4_INSTANCE_BY_V2_API)
                        .mock(Api.V1.Instance.ExtendedDescription(), JSON_EXTENDED_DESCRIPTION)
                        .build(),
                )

                it("returns detail of instance") {
                    val instances = flowToList(repository.instance())

                    runBlocking {
                        repository.fetchInstance(DOMAIN_SAMPLE_COM)

                        instances should haveSize(2)
                        instances should matchInOrder(
                            { it should beNull() },
                            { it!!.domain should be(DOMAIN_SAMPLE_COM) },
                        )
                    }
                }
            }

            context("when the server returns invalid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), HttpStatusCode.BadRequest)
                        .mock(Api.V1.Instance.ExtendedDescription(), HttpStatusCode.BadRequest)
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.instance())

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            runBlocking { repository.fetchInstance(DOMAIN_SAMPLE_COM) }
                        }

                        instances should haveSize(1)
                        instances should matchInOrder(
                            { it should beNull() },
                        )
                    }
                }
            }
        }

        context("for v3") {
            context("when the server returns valid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), JSON_V3_INSTANCE_BY_V1_API)
                        .build(),
                )

                it("returns detail of instance") {
                    val instances = flowToList(repository.instance())

                    runBlocking {
                        repository.fetchInstance(DOMAIN_V3_INSTANCE)

                        instances should haveSize(2)
                        instances should matchInOrder(
                            { it should beNull() },
                            { it!!.domain should be(DOMAIN_V3_INSTANCE) },
                        )
                    }
                }
            }

            context("when the server returns invalid response") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Instance(), HttpStatusCode.BadRequest)
                        .mock(Api.V1.Instance.ExtendedDescription(), HttpStatusCode.NotFound)
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.instance())

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            runBlocking { repository.fetchInstance(DOMAIN_V3_INSTANCE) }
                        }

                        instances should haveSize(1)
                        instances should matchInOrder(
                            { it should beNull() },
                        )
                    }
                }
            }
        }
    }

    describe("#fetchStatuses") {
        context("when the server returns valid response") {
            val repository = buildRepository(
                MockHttpClientEngine
                    .mockGetInitialStatuses()
                    .build(),
            )

            it("returns the statuses") {
                val statuses = flowToList(repository.statuses())

                runBlocking {
                    repository.fetchStatuses(DOMAIN_SAMPLE_COM)

                    statuses should haveSize(2)
                    statuses should matchInOrder(
                        { it should beEmpty() },
                        { it should containExactly(status) },
                    )
                }
            }
        }

        context("when the server returns invalid response") {
            val repository = buildRepository(
                MockHttpClientEngine
                    .mock(Api.V1.Timelines.Public(), HttpStatusCode.BadRequest)
                    .build(),
            )

            it("raises HttpException") {
                val statuses = flowToList(repository.statuses())

                runBlocking {
                    shouldThrowExactly<HttpException> {
                        repository.fetchStatuses(DOMAIN_SAMPLE_COM)
                    }

                    statuses should haveSize(1)
                    statuses should matchInOrder(
                        { it should beEmpty() },
                    )
                }
            }
        }
    }

    describe("#fetchMoreStatuses") {
        context("when the server returns valid response") {
            val repository = buildRepository(
                MockHttpClientEngine
                    .mockGetInitialStatuses()
                    .mockGetNextStatuses()
                    .build(),
            )

            it("returns the statuses") {
                val statuses = flowToList(repository.statuses())

                runBlocking {
                    repository.fetchStatuses(DOMAIN_SAMPLE_COM)
                    repository.fetchMoreStatuses(DOMAIN_SAMPLE_COM)

                    statuses should haveSize(3)
                    statuses should matchInOrder(
                        { it should beEmpty() },
                        { it should containExactly(status) },
                        { it should containExactly(status, prevStatus) },
                    )
                }
            }
        }

        context("when the server returns invalid response") {
            val repository = buildRepository(
                MockHttpClientEngine
                    .mockGetInitialStatuses()
                    .mockNextStatuses(HttpStatusCode.BadRequest)
                    .build(),
            )

            it("raises HttpException") {
                val statuses = flowToList(repository.statuses())

                runBlocking {
                    shouldThrowExactly<HttpException> {
                        repository.fetchStatuses(DOMAIN_SAMPLE_COM)
                        repository.fetchMoreStatuses(DOMAIN_SAMPLE_COM)
                    }

                    statuses should haveSize(2)
                    statuses should matchInOrder(
                        { it should beEmpty() },
                        { it should containExactly(status) },
                    )
                }
            }
        }
    }
})

private fun MockHttpClientEngine.mockGetInitialStatuses() = mock(
    Api.V1.Timelines.Public(),
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("local", "true")
            append("only_media", "false")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_NORMAL]",
)

private fun MockHttpClientEngine.Builder.mockGetNextStatuses() = mock(
    Api.V1.Timelines.Public(),
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("local", "true")
            append("only_media", "false")
            append("max_id", status.id.value)
            append("limit", "20")
        }
    } to "[$JSON_STATUS_NORMAL_ID_99]",
)

private fun MockHttpClientEngine.Builder.mockNextStatuses(
    httpStatusCode: HttpStatusCode,
) = mock(
    Api.V1.Timelines.Public(),
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("local", "true")
            append("only_media", "false")
            append("max_id", status.id.value)
            append("limit", "20")
        }
    } to httpStatusCode,
)

private fun buildRepository(
    mockEngine: MockEngine,
): InstanceRepository {
    val json = Json {
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    val component = TestDataComponent(
        mockEngine,
        buildEmptyMockAuthenticationTokenDataStore(),
    ) {
        AuthorizationApiModule(buildHttpClient(json, mockEngine))
        InstancesSocialApiModule(buildHttpClient(json, mockEngine), "dummy_token")
    }

    return InstanceRepositoryImpl(
        component.get(),
        component.get(),
        component.get(),
    )
}
