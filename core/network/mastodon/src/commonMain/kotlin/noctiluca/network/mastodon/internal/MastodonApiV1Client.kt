package noctiluca.network.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import noctiluca.network.mastodon.Api
import noctiluca.network.mastodon.AuthenticationTokenProvider
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.account.NetworkAccountCredential
import noctiluca.network.mastodon.data.account.NetworkRelationship
import noctiluca.network.mastodon.data.extendeddescription.NetworkExtendedDescription
import noctiluca.network.mastodon.data.instance.NetworkV1Instance
import noctiluca.network.mastodon.data.status.NetworkStatus
import noctiluca.network.mastodon.data.status.NetworkStatusesContext

internal class MastodonApiV1Client(
    token: AuthenticationTokenProvider,
    private val client: HttpClient,
) : MastodonApiV1, AbstractMastodonApiClient(token) {
    override suspend fun getInstance(
        domain: String,
    ): NetworkV1Instance = client.get(
        Api.V1.Instance(),
        domain = domain,
        skipAuthorization = true,
    ).body()

    override suspend fun getInstanceExtendedDescription(
        domain: String,
    ): NetworkExtendedDescription = client.get(
        Api.V1.Instance.ExtendedDescription(),
        domain = domain,
        skipAuthorization = true,
    ).body()

    override suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String?,
    ): NetworkAccountCredential = client.get(
        Api.V1.Accounts.VerifyCredentials(),
        domain = domain,
        accessToken = accessToken,
    ).body()

    override suspend fun getTimelinesPublic(
        local: Boolean,
        remote: Boolean,
        onlyMedia: Boolean,
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int,
    ): List<NetworkStatus> = client.get(
        Api.V1.Timelines.Public(),
    ) {
        parameter("local", local.toString())
        parameter("remote", remote.toString())
        parameter("only_media", onlyMedia.toString())
        parameter("max_id", maxId)
        parameter("since_id", sinceId)
        parameter("min_id", minId)
        parameter("limit", limit.toString())
    }.body()

    override suspend fun getTimelinesPublic(
        domain: String,
        maxId: String?,
        limit: Int,
    ): List<NetworkStatus> = client.get(
        Api.V1.Timelines.Public(),
        domain = domain,
        skipAuthorization = true,
    ) {
        parameter("local", true)
        parameter("only_media", false)
        parameter("max_id", maxId)
        parameter("limit", limit.toString())
    }.body()

    override suspend fun getTimelinesHome(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int
    ): List<NetworkStatus> = client.get(
        Api.V1.Timelines.Home()
    ) {
        parameter("max_id", maxId)
        parameter("since_id", sinceId)
        parameter("min_id", minId)
        parameter("limit", limit.toString())
    }.body()

    override suspend fun getStatus(
        id: String
    ): NetworkStatus = client.get(
        Api.V1.Statuses.Id(id = id),
    ).body()

    override suspend fun deleteStatus(
        id: String,
    ): NetworkStatus = client.delete(
        Api.V1.Statuses.Id(id = id),
    ).body()

    override suspend fun getStatusesContext(
        id: String,
    ): NetworkStatusesContext = client.get(
        Api.V1.Statuses.Id.Context(id = id),
    ).body()

    override suspend fun postStatusesFavourite(
        id: String,
    ): NetworkStatus = client.post(
        Api.V1.Statuses.Id.Favourite(id = id),
    ).body()

    override suspend fun postStatusesUnfavourite(
        id: String,
    ): NetworkStatus = client.post(
        Api.V1.Statuses.Id.Unfavourite(id = id),
    ).body()

    override suspend fun postStatusesReblog(
        id: String,
    ): NetworkStatus = client.post(
        Api.V1.Statuses.Id.Reblog(id = id),
    ).body()

    override suspend fun postStatusesUnreblog(
        id: String,
    ): NetworkStatus = client.post(
        Api.V1.Statuses.Id.Unreblog(id = id),
    ).body()

    override suspend fun postStatusesBookmark(
        id: String,
    ): NetworkStatus = client.post(
        Api.V1.Statuses.Id.Bookmark(id = id),
    ).body()

    override suspend fun postStatusesUnbookmark(
        id: String,
    ): NetworkStatus = client.post(
        Api.V1.Statuses.Id.Unbookmark(id = id),
    ).body()

    override suspend fun getAccount(
        id: String,
    ): NetworkAccount = client.get(
        Api.V1.Accounts.Id(id = id),
    ).body()

    override suspend fun getAccountsRelationships(
        id: List<String>
    ): List<NetworkRelationship> = client.get(
        Api.V1.Accounts.Relationships(),
    ) {
        parameter("id", id)
    }.body()

    override suspend fun getAccountsStatuses(
        id: String,
        maxId: String?,
        onlyMedia: Boolean,
        excludeReplies: Boolean,
        limit: Int,
    ): List<NetworkStatus> = client.get(
        Api.V1.Accounts.Id.Statuses(Api.V1.Accounts.Id(id = id))
    ) {
        parameter("max_id", maxId)
        parameter("only_media", onlyMedia)
        if (!onlyMedia) {
            parameter("exclude_replies", excludeReplies)
            parameter("exclude_reblogs", false)
        }
        parameter("limit", limit.toString())
    }.body()
}
