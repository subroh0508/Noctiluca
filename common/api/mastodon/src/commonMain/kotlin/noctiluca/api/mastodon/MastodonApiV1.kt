package noctiluca.api.mastodon

import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.status.StatusJson

interface MastodonApiV1 {
    suspend fun getInstance(
        domain: String,
    ): V1InstanceJson

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
}
