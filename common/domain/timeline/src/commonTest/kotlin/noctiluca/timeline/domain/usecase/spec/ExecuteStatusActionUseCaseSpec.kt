package noctiluca.timeline.domain.usecase.spec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.resources.serialization.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.api.mastodon.Api
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.status.model.Status
import noctiluca.status.model.Tooter
import noctiluca.test.DOMAIN_MASTODON_JP
import noctiluca.test.URL_MASTODON_JP
import noctiluca.test.util.isMatched
import noctiluca.timeline.domain.TestTimelineUseCaseComponent
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.usecase.ExecuteStatusActionUseCase
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_BOOKMARK
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_FAVOURITE
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_UNBOOKMARK
import noctiluca.timeline.domain.usecase.json.JSON_STATUSES_UNFAVOURITE

class ExecuteStatusActionUseCaseSpec : DescribeSpec({
    val tooter = Tooter(
        AccountId("2"),
        "piyo@$DOMAIN_MASTODON_JP",
        "Kotori Otonashi",
        Uri("$URL_MASTODON_JP/@piyo"),
        Uri("$URL_MASTODON_JP/accounts/avatars/avater.jpg"),
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

    val format = ResourcesFormat()

    val component = TestTimelineUseCaseComponent(
        MockEngine { request ->
            val json = when {
                request.url.isMatched(
                    format,
                    Api.V1.Statuses.Id.Favourite(status.id.value),
                ) -> JSON_STATUSES_FAVOURITE
                request.url.isMatched(
                    format,
                    Api.V1.Statuses.Id.Unfavourite(status.id.value),
                ) -> JSON_STATUSES_UNFAVOURITE
                request.url.isMatched(
                    format,
                    Api.V1.Statuses.Id.Reblog(status.id.value),
                ) -> """{"id":"1"}"""
                request.url.isMatched(
                    format,
                    Api.V1.Statuses.Id.Unreblog(status.id.value),
                ) -> """{"id":"1"}"""
                request.url.isMatched(
                    format,
                    Api.V1.Statuses.Id.Bookmark(status.id.value),
                ) -> JSON_STATUSES_BOOKMARK
                request.url.isMatched(
                    format,
                    Api.V1.Statuses.Id.Unbookmark(status.id.value),
                ) -> JSON_STATUSES_UNBOOKMARK
                else -> error("Unexpected path: ${request.url}")
            }

            respond(
                content = ByteReadChannel(json),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
    )

    val useCase: ExecuteStatusActionUseCase = component.scope.get()

    describe("#execute") {
        context("when action is favourite") {
            it("returns favourited status") {
                runBlocking {
                    useCase.execute(
                        status.copy(favourited = false, favouriteCount = 0),
                        StatusAction.FAVOURITE,
                    )
                } should be(status.copy(favourited = true, favouriteCount = 1))
            }

            it("returns unfavourited status") {
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
                runBlocking {
                    useCase.execute(
                        status.copy(bookmarked = false),
                        StatusAction.BOOKMARK,
                    )
                } should be(status.copy(bookmarked = true))
            }

            it("returns unbookmarked status") {
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
