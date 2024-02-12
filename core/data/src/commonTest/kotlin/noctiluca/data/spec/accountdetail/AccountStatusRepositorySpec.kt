package noctiluca.data.spec.accountdetail

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import noctiluca.data.TestDataComponent
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.accountdetail.impl.AccountStatusRepositoryImpl
import noctiluca.model.HttpException
import noctiluca.model.HttpUnauthorizedException
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.network.mastodon.Api
import noctiluca.test.JSON_STATUS_MEDIA
import noctiluca.test.JSON_STATUS_MEDIA_ID_199
import noctiluca.test.JSON_STATUS_NORMAL
import noctiluca.test.JSON_STATUS_NORMAL_ID_99
import noctiluca.test.extension.flowToList
import noctiluca.test.mock.MockAuthorizationTokenDataStore
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.mock.buildFilledMockAuthenticationTokenDataStore
import org.koin.core.component.get

class AccountStatusRepositorySpec : DescribeSpec({
    coroutineTestScope = true

    describe("#statuses") {
        context("when the server returns valid response") {
            context("and the query does not have max_id") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mockGetInitialStatuses()
                        .build(),
                )

                it("returns the statuses") {
                    flowToList(repository.statuses(myAccount.id)).let {
                        it should haveSize(1)
                        it.first() should be(
                            mapOf(
                                StatusesQuery.DEFAULT to listOf(status),
                                StatusesQuery.WITH_REPLIES to listOf(status),
                                StatusesQuery.ONLY_MEDIA to listOf(media),
                            ),
                        )
                    }
                }
            }
        }
        context("when the server returns invalid response") {
            val repository = buildRepository(
                MockHttpClientEngine
                    .mock(
                        Api.V1.Accounts.Id.Statuses(
                            Api.V1.Accounts.Id(
                                id = myAccount.id.value
                            ),
                        ),
                        HttpStatusCode.Unauthorized,
                    )
                    .build(),
            )

            it("raises HttpUnauthorizedException") {
                shouldThrowExactly<HttpUnauthorizedException> {
                    runBlocking { repository.statuses(myAccount.id).first() }
                }
            }
        }
    }

    describe("#loadStatuses") {
        context("when the server returns valid response") {
            context("and the query has max_id") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mockGetInitialStatuses()
                        .mockGetNextStatuses()
                        .build(),
                )

                it("returns the statuses") {
                    val instances = flowToList(repository.statuses(myAccount.id))

                    runBlocking {
                        StatusesQuery.entries.forEach { query ->
                            repository.loadStatuses(myAccount.id, query)
                        }

                        instances.let {
                            it should haveSize(4)
                            it.last() should be(
                                mapOf(
                                    StatusesQuery.DEFAULT to listOf(status, prevStatus),
                                    StatusesQuery.WITH_REPLIES to listOf(status, prevStatus),
                                    StatusesQuery.ONLY_MEDIA to listOf(media, prevMedia),
                                ),
                            )
                        }
                    }
                }
            }
        }
        context("when the server returns invalid response") {
            context("and the query has max_id") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mockGetInitialStatuses()
                        .mockGetNextStatuses(HttpStatusCode.BadRequest)
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.statuses(myAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            StatusesQuery.entries.forEach { query ->
                                repository.loadStatuses(myAccount.id, query)
                            }
                        }

                        instances.let {
                            it should haveSize(1)
                            it.last() should be(
                                mapOf(
                                    StatusesQuery.DEFAULT to listOf(status),
                                    StatusesQuery.WITH_REPLIES to listOf(status),
                                    StatusesQuery.ONLY_MEDIA to listOf(media),
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
})

private fun MockHttpClientEngine.mockGetInitialStatuses() = mock(
    Api.V1.Accounts.Id.Statuses(
        Api.V1.Accounts.Id(
            id = myAccount.id.value,
        ),
    ),
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("only_media", "false")
            append("exclude_replies", "true")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_NORMAL]",
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("only_media", "false")
            append("exclude_replies", "false")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_NORMAL]",
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("only_media", "true")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_MEDIA]",
)

private fun MockHttpClientEngine.Builder.mockGetNextStatuses() = mock(
    Api.V1.Accounts.Id.Statuses(
        Api.V1.Accounts.Id(
            id = myAccount.id.value,
        ),
    ),
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("max_id", status.id.value)
            append("only_media", "false")
            append("exclude_replies", "true")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_NORMAL_ID_99]",
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("max_id", status.id.value)
            append("only_media", "false")
            append("exclude_replies", "false")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_NORMAL_ID_99]",
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("max_id", media.id.value)
            append("only_media", "true")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_MEDIA_ID_199]",
)

private fun MockHttpClientEngine.Builder.mockGetNextStatuses(
    httpStatusCode: HttpStatusCode,
) = mock(
    Api.V1.Accounts.Id.Statuses(
        Api.V1.Accounts.Id(
            id = myAccount.id.value,
        ),
    ),
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("max_id", status.id.value)
            append("only_media", "false")
            append("exclude_replies", "true")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to httpStatusCode,
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("max_id", status.id.value)
            append("only_media", "false")
            append("exclude_replies", "false")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to httpStatusCode,
    { parameters: ParametersBuilder ->
        with(parameters) {
            append("max_id", media.id.value)
            append("only_media", "true")
            append("exclude_replies", "true")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to httpStatusCode,
)

private fun buildRepository(
    mockEngine: MockEngine,
    mockAuthenticationTokenDataStore: MockAuthorizationTokenDataStore = buildFilledMockAuthenticationTokenDataStore(),
): AccountStatusRepository {
    val component = TestDataComponent(
        mockEngine,
        mockAuthenticationTokenDataStore,
    )

    return AccountStatusRepositoryImpl(
        component.get(),
        component.get(),
    )
}
