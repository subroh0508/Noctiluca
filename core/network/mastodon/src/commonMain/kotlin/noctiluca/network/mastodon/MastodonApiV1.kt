package noctiluca.network.mastodon

import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.account.NetworkAccountCredential
import noctiluca.network.mastodon.data.account.NetworkRelationship
import noctiluca.network.mastodon.data.extendeddescription.NetworkExtendedDescription
import noctiluca.network.mastodon.data.instance.NetworkV1Instance
import noctiluca.network.mastodon.data.status.NetworkStatus

@Suppress("TooManyFunctions")
interface MastodonApiV1 {
    suspend fun getInstance(
        domain: String,
    ): NetworkV1Instance

    suspend fun getInstanceExtendedDescription(
        domain: String,
    ): NetworkExtendedDescription

    suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String? = null,
    ): NetworkAccountCredential

    suspend fun getTimelinesPublic(
        local: Boolean = false,
        remote: Boolean = false,
        onlyMedia: Boolean = false,
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int = 20,
    ): List<NetworkStatus>

    suspend fun getTimelinesPublic(
        domain: String,
        maxId: String? = null,
        limit: Int = 20,
    ): List<NetworkStatus>

    suspend fun getTimelinesHome(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int = 20,
    ): List<NetworkStatus>

    suspend fun postStatusesFavourite(
        id: String,
    ): NetworkStatus

    suspend fun postStatusesUnfavourite(
        id: String,
    ): NetworkStatus

    suspend fun postStatusesReblog(
        id: String,
    ): NetworkStatus

    suspend fun postStatusesUnreblog(
        id: String,
    ): NetworkStatus

    suspend fun postStatusesBookmark(
        id: String,
    ): NetworkStatus

    suspend fun postStatusesUnbookmark(
        id: String,
    ): NetworkStatus

    suspend fun getAccount(
        id: String,
    ): NetworkAccount

    suspend fun getAccountsRelationships(
        id: List<String>,
    ): List<NetworkRelationship>

    suspend fun getAccountsStatuses(
        id: String,
        maxId: String? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = true,
        limit: Int = 20,
    ): List<NetworkStatus>
}
