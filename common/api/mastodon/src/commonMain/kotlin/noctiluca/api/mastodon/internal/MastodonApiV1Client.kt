package noctiluca.api.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.repository.TokenCache

internal class MastodonApiV1Client(
    private val token: TokenCache,
    private val client: HttpClient,
) : MastodonApiV1 {
    override suspend fun getInstance(
        domain: String,
    ): V1InstanceJson = client.get(
        Api.V1.Instance(),
        domain = domain,
        skipAuthorization = true,
    ).body()

    override suspend fun getVerifyAccountsCredentials(
        domain: String,
    ): AccountCredentialJson = client.get(
        Api.V1.Accounts.VerifyCredentials(),
        domain = domain,
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

    private suspend inline fun <reified T: Any> HttpClient.get(
        resource: T,
        domain: String? = null,
        skipAuthorization: Boolean = false,
        httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
    ) = get(resource, builder = {
        setAccessTokenAndHost(domain, skipAuthorization)
        httpRequestBuilder()
    })

    private suspend inline fun <reified T: Any, reified E: Any> HttpClient.post(
        resource: T,
        body: E,
        domain: String? = null,
        skipAuthorization: Boolean = false,
    ) = post(resource) {
        setAccessTokenAndHost(domain, skipAuthorization)
        setBody(body)
    }

    private suspend fun HttpRequestBuilder.setAccessTokenAndHost(
        domain: String?,
        skipAuthorization: Boolean,
    ) {
        val token = getCurrentAccessToken()
        val host = domain ?: getCurrentDomain()

        if (host != null) {
            this.host = host
        }

        if (token != null && !skipAuthorization) {
            bearerAuth(token)
        }
    }


    private suspend fun getCurrentAccessToken() = token.getCurrentAccessToken()
    private suspend fun getCurrentDomain() = token.getCurrentDomain()
}