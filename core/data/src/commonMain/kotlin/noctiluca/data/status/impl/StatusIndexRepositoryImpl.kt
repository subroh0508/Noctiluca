package noctiluca.data.status.impl

import noctiluca.data.status.StatusIndexRepository
import noctiluca.data.status.toEntity
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.network.mastodon.MastodonApiV1

internal class StatusIndexRepositoryImpl(
    private val api: MastodonApiV1,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
) : StatusIndexRepository {
    override suspend fun fetchGlobal(
        onlyRemote: Boolean,
        onlyMedia: Boolean,
        maxId: StatusId?,
    ) = api.getTimelinesPublic(
        local = false,
        remote = onlyRemote,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }

    override suspend fun fetchLocal(
        onlyMedia: Boolean,
        maxId: StatusId?,
    ) = api.getTimelinesPublic(
        local = true,
        remote = false,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }

    override suspend fun fetchHome(
        maxId: StatusId?,
    ) = api.getTimelinesHome(
        maxId = maxId?.value,
    ).map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }
}
