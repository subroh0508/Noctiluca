package noctiluca.data.spec.accountdetail

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.data.TestDataComponent
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.accountdetail.impl.AccountStatusRepositoryImpl
import noctiluca.model.HttpUnauthorizedException
import noctiluca.model.StatusId
import noctiluca.model.account.Account
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status
import noctiluca.network.mastodon.Api
import noctiluca.test.JSON_STATUS_MEDIA
import noctiluca.test.JSON_STATUS_NORMAL
import noctiluca.test.JSON_STATUS_NORMAL_ID_99
import noctiluca.test.mock.MockAuthenticationTokenDataStore
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.mock.buildFilledMockAuthenticationTokenDataStore
import org.koin.core.component.get

class AccountStatusRepositorySpec : DescribeSpec({
    val status = Status(
        StatusId("100"),
        "<p>Test Status</p>",
        warningText = null,
        "2023-03-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
        Status.Visibility.PUBLIC,
        0,
        0,
        0,
        favourited = false,
        reblogged = false,
        bookmarked = false,
        tooter = Account(
            myAccount.id,
            myAccount.username,
            myAccount.displayName,
            myAccount.url,
            myAccount.avatar,
            myAccount.screen,
        ),
        rebloggedBy = null,
        via = Status.Via("Web", website = null),
    )

    val prevStatus = Status(
        StatusId("99"),
        "<p>Test Status</p>",
        warningText = null,
        "2023-02-28T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
        Status.Visibility.PUBLIC,
        0,
        0,
        0,
        favourited = false,
        reblogged = false,
        bookmarked = false,
        tooter = Account(
            myAccount.id,
            myAccount.username,
            myAccount.displayName,
            myAccount.url,
            myAccount.avatar,
            myAccount.screen,
        ),
        rebloggedBy = null,
        via = Status.Via("Web", website = null),
    )

    val media = Status(
        StatusId("200"),
        "<p>Test Media</p>",
        warningText = null,
        "2023-03-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
        Status.Visibility.PUBLIC,
        0,
        0,
        0,
        favourited = false,
        reblogged = false,
        bookmarked = false,
        tooter = Account(
            myAccount.id,
            myAccount.username,
            myAccount.displayName,
            myAccount.url,
            myAccount.avatar,
            myAccount.screen,
        ),
        rebloggedBy = null,
        via = Status.Via("Web", website = null),
    )

    describe("#execute") {
        context("when the server returns valid response") {
            context("and the query does not have max_id") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mockGetInitialStatuses()
                        .build(),
                )

                it("returns the statuses") {
                    runBlocking {
                        repository.statuses(myAccount.id).first()
                    } should be(
                        mapOf(
                            StatusesQuery.DEFAULT to listOf(status),
                            StatusesQuery.WITH_REPLIES to listOf(status),
                            StatusesQuery.ONLY_MEDIA to listOf(media),
                        ),
                    )
                }
            }

            xcontext("and the query has max_id") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mockGetInitialStatuses()
                        .mock(
                            Api.V1.Accounts.Id.Statuses(
                                Api.V1.Accounts.Id(
                                    id = myAccount.id.value,
                                )
                            ),
                            { parameters: ParametersBuilder ->
                                with(parameters) {
                                    append("max_id", status.id.value)
                                    append("only_media", "false")
                                    append("exclude_replies", "true")
                                    append("exclude_reblogs", "false")
                                    append("limit", "20")
                                }
                            } to "[$JSON_STATUS_NORMAL_ID_99]"
                        )
                        .build(),
                )

                it("returns the statuses") {
                    val instances = mutableListOf(mapOf<StatusesQuery, List<Status>>())

                    runBlocking {
                        launch {
                            repository.statuses(myAccount.id).toList(instances)
                        }
                        repository.loadStatuses(myAccount.id, StatusesQuery.DEFAULT)
                        instances.last()
                    } should be(
                        mapOf(
                            StatusesQuery.DEFAULT to listOf(status, prevStatus),
                            StatusesQuery.WITH_REPLIES to listOf(status),
                            StatusesQuery.ONLY_MEDIA to listOf(media),
                        ),
                    )
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
                            )
                        ),
                        HttpStatusCode.Unauthorized
                    )
                    .build(),
            )

            it("raises HttpUnauthorizedException") {
                shouldThrowExactly<HttpUnauthorizedException> {
                    runBlocking {
                        repository.statuses(myAccount.id).first()
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
        )
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
            append("exclude_replies", "true")
            append("exclude_reblogs", "false")
            append("limit", "20")
        }
    } to "[$JSON_STATUS_MEDIA]",
)

private fun buildRepository(
    mockEngine: MockEngine,
    mockAuthenticationTokenDataStore: MockAuthenticationTokenDataStore = buildFilledMockAuthenticationTokenDataStore(),
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
