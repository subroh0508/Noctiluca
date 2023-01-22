package noctiluca.api.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.api.mastodon.json.streaming.StreamEventJson
import noctiluca.repository.TokenCache

internal class MastodonApiV1Client(
    private val token: TokenCache,
    private val client: HttpClient,
    private val json: Json,
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

    override suspend fun streaming(
        stream: String,
        type: String,
        listId: String?,
        tag: String?,
    ): Flow<StreamEventJson> = flow {
        client.websocket(
            Api.V1.Streaming(),
            httpRequestBuilder = {
                parameter("type", type)
                parameter("stream", stream)
                parameter("list", listId)
                parameter("tag", tag)
            },
        ) {
            val frame = incoming.receive()
            if (frame is Frame.Text) {
                val text = frame.readText()

                println(text)
                emit(json.decodeFromString(StreamEventJson.serializer(), text))
            }
        }
    }

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

    private suspend inline fun <reified T: Any> HttpClient.websocket(
        resource: T,
        domain: String? = null,
        skipAuthorization: Boolean = false,
        crossinline httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
        noinline block: suspend DefaultClientWebSocketSession.() -> Unit,
    ) = webSocket(
        {
            method = HttpMethod.Get
            val (host, token) = runBlocking {
                getCurrentAccessToken() to (domain ?: getCurrentDomain())
            }

            if (host != null) {
                this.host = host
            }

            href(client.plugin(Resources).resourcesFormat, resource)

            if (token != null && !skipAuthorization) {
                parameter("access_token", host)
            }
            httpRequestBuilder()
        },
        block,
    )

    private suspend fun HttpRequestBuilder.setAccessTokenAndHost(
        domain: String? = null,
        skipAuthorization: Boolean= false,
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