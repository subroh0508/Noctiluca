package noctiluca.network.mastodon

import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.account.NetworkAccountCredential
import noctiluca.network.mastodon.data.account.NetworkRelationship
import noctiluca.network.mastodon.data.extendeddescription.NetworkExtendedDescription
import noctiluca.network.mastodon.data.instance.NetworkV1Instance
import noctiluca.network.mastodon.data.status.NetworkStatus
import noctiluca.network.mastodon.data.status.NetworkStatusesContext

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

    suspend fun getStatus(
        id: String,
    ): NetworkStatus

    suspend fun deleteStatus(
        id: String
    ): NetworkStatus

    suspend fun getStatusesContext(
        id: String,
    ): NetworkStatusesContext

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

    suspend fun postAccountsFollow(
        id: String,
        reblogs: Boolean = true,
        notify: Boolean = false,
    ): NetworkRelationship

    suspend fun postAccountsUnfollow(
        id: String,
    ): NetworkRelationship

    suspend fun postAccountsBlock(
        id: String,
    ): NetworkRelationship

    suspend fun postAccountsUnblock(
        id: String,
    ): NetworkRelationship

    suspend fun postAccountsMute(
        id: String,
    ): NetworkRelationship

    suspend fun postAccountsUnmute(
        id: String,
    ): NetworkRelationship

    suspend fun getAccountsStatuses(
        id: String,
        maxId: String? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = true,
        limit: Int = 20,
    ): List<NetworkStatus>
}
