package noctiluca.network.mastodon

import noctiluca.network.mastodon.json.account.AccountCredentialJson
import noctiluca.network.mastodon.json.account.AccountJson
import noctiluca.network.mastodon.json.account.RelationshipJson
import noctiluca.network.mastodon.json.extendeddescription.ExtendedDescriptionJson
import noctiluca.network.mastodon.json.instance.V1InstanceJson
import noctiluca.network.mastodon.json.status.StatusJson

@Suppress("TooManyFunctions")
interface MastodonApiV1 {
    suspend fun getInstance(
        domain: String,
    ): V1InstanceJson

    suspend fun getInstanceExtendedDescription(
        domain: String,
    ): ExtendedDescriptionJson

    suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String? = null,
    ): AccountCredentialJson

    suspend fun getTimelinesPublic(
        local: Boolean = false,
        remote: Boolean = false,
        onlyMedia: Boolean = false,
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int = 20,
    ): List<StatusJson>

    suspend fun getTimelinesPublic(
        domain: String,
        maxId: String? = null,
        limit: Int = 20,
    ): List<StatusJson>

    suspend fun getTimelinesHome(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int = 20,
    ): List<StatusJson>

    suspend fun postStatusesFavourite(
        id: String,
    ): StatusJson

    suspend fun postStatusesUnfavourite(
        id: String,
    ): StatusJson

    suspend fun postStatusesReblog(
        id: String,
    ): StatusJson

    suspend fun postStatusesUnreblog(
        id: String,
    ): StatusJson

    suspend fun postStatusesBookmark(
        id: String,
    ): StatusJson

    suspend fun postStatusesUnbookmark(
        id: String,
    ): StatusJson

    suspend fun getAccount(
        id: String,
    ): AccountJson

    suspend fun getAccountsRelationships(
        id: List<String>,
    ): List<RelationshipJson>

    suspend fun getAccountsStatuses(
        id: String,
        maxId: String? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = true,
        limit: Int = 20,
    ): List<StatusJson>
}
