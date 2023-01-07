package noctiluca.status.infra.repository.impl

import kotlinx.datetime.LocalDateTime
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountJson
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.repository.TokenProvider
import noctiluca.status.infra.repository.TimelineRepository
import noctiluca.status.model.Status
import noctiluca.status.model.Tooter

internal class TimelineRepositoryImpl(
    private val api: MastodonApiV1,
    private val tokenProvider: TokenProvider,
) : TimelineRepository {
    override suspend fun fetchLocal(
        onlyMedia: Boolean,
        maxId: StatusId?,
    ) = api.getTimelinesPublic(
        local = true,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenProvider.getCurrent()?.id) }

    override suspend fun fetchGlobal(
        remote: Boolean,
        onlyMedia: Boolean,
        maxId: StatusId?
    ) = api.getTimelinesPublic(
        remote = remote,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenProvider.getCurrent()?.id) }

    private fun StatusJson.toEntity(newAccountId: AccountId?) = Status(
        (reblog?.id ?: id).let(::StatusId),
        reblog?.content ?: content,
        spoilerText,
        LocalDateTime.parse(reblog?.createdAt ?: createdAt),
        Status.Visibility.valueOf(visibility.uppercase()),
        reblog?.repliesCount ?: repliesCount,
        reblog?.favouritesCount ?: favouritesCount,
        reblog?.reblogsCount ?: reblogsCount,
        (reblog?.favourited ?: favourited) ?: false,
        (reblog?.reblogged ?: reblogged) ?: false,
        (reblog?.account ?: account).toTooter(),
        if (reblog != null && newAccountId?.value != reblog?.account?.id)
            reblog?.account?.toTooter()
        else
            null,
    )

    private fun AccountJson.toTooter() = Tooter(
        AccountId(id),
        username,
        displayName,
        Uri(url),
        Uri(avatar),
    )
}