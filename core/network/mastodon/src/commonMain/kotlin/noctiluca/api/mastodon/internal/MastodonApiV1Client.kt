package noctiluca.api.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import noctiluca.api.mastodon.Api
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.TokenProvider
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.account.AccountJson
import noctiluca.api.mastodon.json.account.RelationshipJson
import noctiluca.api.mastodon.json.extendeddescription.ExtendedDescriptionJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.status.StatusJson

internal class MastodonApiV1Client(
    override val token: TokenProvider,
    override val client: HttpClient,
) : MastodonApiV1, AbstractMastodonApiClient() {
    override suspend fun getInstance(
        domain: String,
    ): V1InstanceJson = client.get(
        Api.V1.Instance(),
        domain = domain,
        skipAuthorization = true,
    ).body()

    override suspend fun getInstanceExtendedDescription(
        domain: String,
    ): ExtendedDescriptionJson = client.get(
        Api.V1.Instance.ExtendedDescription(),
        domain = domain,
        skipAuthorization = true,
    ).body()

    override suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String?,
    ): AccountCredentialJson = client.get(
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
    ): List<StatusJson> = client.get(
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
    ): List<StatusJson> = client.get(
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
    ): List<StatusJson> = client.get(
        Api.V1.Timelines.Home()
    ) {
        parameter("max_id", maxId)
        parameter("since_id", sinceId)
        parameter("min_id", minId)
        parameter("limit", limit.toString())
    }.body()

    override suspend fun postStatusesFavourite(
        id: String,
    ): StatusJson = client.post(
        Api.V1.Statuses.Id.Favourite(id = id),
    ).body()

    override suspend fun postStatusesUnfavourite(
        id: String,
    ): StatusJson = client.post(
        Api.V1.Statuses.Id.Unfavourite(id = id),
    ).body()

    override suspend fun postStatusesReblog(
        id: String,
    ): StatusJson = client.post(
        Api.V1.Statuses.Id.Reblog(id = id),
    ).body()

    override suspend fun postStatusesUnreblog(
        id: String,
    ): StatusJson = client.post(
        Api.V1.Statuses.Id.Unreblog(id = id),
    ).body()

    override suspend fun postStatusesBookmark(
        id: String,
    ): StatusJson = client.post(
        Api.V1.Statuses.Id.Bookmark(id = id),
    ).body()

    override suspend fun postStatusesUnbookmark(
        id: String,
    ): StatusJson = client.post(
        Api.V1.Statuses.Id.Unbookmark(id = id),
    ).body()

    override suspend fun getAccount(
        id: String,
    ): AccountJson = client.get(
        Api.V1.Accounts.Id(id = id),
    ).body()

    override suspend fun getAccountsRelationships(
        id: List<String>
    ): List<RelationshipJson> = client.get(
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
    ): List<StatusJson> = client.get(
        Api.V1.Accounts.Id.Statuses(Api.V1.Accounts.Id(id = id))
    ) {
        parameter("max_id", maxId)
        parameter("only_media", onlyMedia)
        parameter("exclude_replies", excludeReplies)
        parameter("exclude_reblogs", false)
        parameter("limit", limit.toString())
    }.body()
}
