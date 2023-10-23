package noctiluca.timeline.domain.usecase.spec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.model.status.Status
import noctiluca.network.mastodon.Api
import noctiluca.test.DOMAIN_MASTODON_JP
import noctiluca.test.URL_MASTODON_JP
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.timeline.domain.TestTimelineUseCaseComponent
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.usecase.ExecuteStatusActionUseCase
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_BOOKMARK
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_FAVOURITE
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_UNBOOKMARK
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_UNFAVOURITE

class ExecuteStatusActionUseCaseSpec : DescribeSpec({
    val tooter = Account(
        AccountId("2"),
        "piyo",
        "Kotori Otonashi",
        Uri("$URL_MASTODON_JP/@piyo"),
        Uri("$URL_MASTODON_JP/accounts/avatars/avater.jpg"),
        "@piyo@$DOMAIN_MASTODON_JP"
    )

    val status = Status(
        StatusId("1"),
        "<p>toot content</p>",
        "<p>warning text</p>",
        "2022-01-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
        Status.Visibility.PUBLIC,
        0,
        0,
        0,
        favourited = false,
        reblogged = false,
        bookmarked = false,
        tooter,
        rebloggedBy = null,
    )

    describe("#execute") {
        context("when action is favourite") {
            it("returns favourited status") {
                val useCase = buildUseCase(
                    Api.V1.Statuses.Id.Favourite(status.id.value),
                    JSON_STATUSES_FAVOURITE,
                )

                runBlocking {
                    useCase.execute(
                        status.copy(favourited = false, favouriteCount = 0),
                        StatusAction.FAVOURITE,
                    )
                } should be(status.copy(favourited = true, favouriteCount = 1))
            }

            it("returns unfavourited status") {
                val useCase = buildUseCase(
                    Api.V1.Statuses.Id.Unfavourite(status.id.value),
                    JSON_STATUSES_UNFAVOURITE,
                )

                runBlocking {
                    useCase.execute(
                        status.copy(favourited = true, favouriteCount = 1),
                        StatusAction.FAVOURITE,
                    )
                } should be(status.copy(favourited = false, favouriteCount = 0))
            }
        }

        context("when action is bookmark") {
            it("returns bookmarked status") {
                val useCase = buildUseCase(
                    Api.V1.Statuses.Id.Bookmark(status.id.value),
                    JSON_STATUSES_BOOKMARK,
                )

                runBlocking {
                    useCase.execute(
                        status.copy(bookmarked = false),
                        StatusAction.BOOKMARK,
                    )
                } should be(status.copy(bookmarked = true))
            }

            it("returns unbookmarked status") {
                val useCase = buildUseCase(
                    Api.V1.Statuses.Id.Unbookmark(status.id.value),
                    JSON_STATUSES_UNBOOKMARK,
                )

                runBlocking {
                    useCase.execute(
                        status.copy(bookmarked = true),
                        StatusAction.BOOKMARK,
                    )
                } should be(status.copy(bookmarked = false))
            }
        }
    }
})

private inline fun <reified T> buildUseCase(
    resource: T,
    expected: String,
): ExecuteStatusActionUseCase = TestTimelineUseCaseComponent(
    MockHttpClientEngine(resource, expected),
).scope.get()
