package noctiluca.data.status.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.LocalDateTime
import noctiluca.data.extensions.toISO8601
import noctiluca.data.status.StatusRepository
import noctiluca.data.status.toEntity
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.status.Poll
import noctiluca.model.status.Status
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.data.status.NetworkStatus
import noctiluca.network.mastodon.paramaters.status.NewStatusParameter

internal class StatusRepositoryImpl(
    private val api: MastodonApiV1,
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
) : StatusRepository {
    private val statusContextStateFlow: MutableStateFlow<List<Status>> by lazy { MutableStateFlow(listOf()) }

    override fun context(id: StatusId) = statusContextStateFlow.onStart {
        val status = fetch(id)
        statusContextStateFlow.value = listOf(status)
        statusContextStateFlow.value = fetchContext(status)
    }

    override suspend fun new(
        status: String,
        spoilerText: String?,
        visibility: Status.Visibility,
        mediaIds: List<String>,
        poll: Poll.New?,
        inReplyToId: StatusId?,
        sensitive: Boolean,
        language: String,
        scheduledAt: LocalDateTime?,
    ) = api.postStatus(
        NewStatusParameter(
            status,
            mediaIds,
            poll?.let {
                NewStatusParameter.Poll(
                    it.options,
                    it.expiresIn,
                    it.multiple,
                    it.hideTotals
                )
            },
            inReplyToId?.value,
            sensitive,
            spoilerText,
            visibility.name.lowercase(),
            language,
            scheduledAt?.toISO8601(),
        )
    ).toEntity()

    override suspend fun delete(
        id: StatusId,
    ) = api.deleteStatus(id.value).toEntity()

    override suspend fun favourite(status: Status): Status {
        val json =
            if (status.favourited) {
                api.postStatusesUnfavourite(status.id.value)
            } else {
                api.postStatusesFavourite(status.id.value)
            }

        return json.toEntity()
    }

    override suspend fun boost(status: Status): Status {
        val json =
            if (status.reblogged) {
                api.postStatusesUnreblog(status.id.value)
            } else {
                api.postStatusesReblog(status.id.value)
            }

        return json.toEntity()
    }

    override suspend fun bookmark(status: Status): Status {
        val json =
            if (status.bookmarked) {
                api.postStatusesUnbookmark(status.id.value)
            } else {
                api.postStatusesBookmark(status.id.value)
            }

        return json.toEntity()
    }

    private suspend fun fetch(id: StatusId) = api.getStatus(id.value).toEntity()

    private suspend fun fetchContext(status: Status): List<Status> {
        val (ancestors, descendants) = api.getStatusesContext(status.id.value).let { (anc, des) ->
            anc.map { it.toEntity() } to des.map { it.toEntity() }
        }

        return ancestors + listOf(status) + descendants
    }

    private suspend fun NetworkStatus.toEntity() = toEntity(authorizationTokenDataStore.getCurrent()?.id)
}
