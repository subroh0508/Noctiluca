package noctiluca.api.mastodon.internal

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.MastodonStream
import noctiluca.api.mastodon.json.streaming.StreamEventJson
import noctiluca.repository.TokenCache

internal class MastodonStreamClient(
    private val token: TokenCache,
    private val client: HttpClient,
    override val json: Json,
) : MastodonStream {
    override suspend fun streaming(
        stream: String,
        type: String,
        listId: String?,
        tag: String?,
    ): Flow<StreamEventJson> = flow {
        client.websocket(
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

    private suspend fun HttpClient.websocket(
        domain: String? = null,
        skipAuthorization: Boolean = false,
        httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
        block: suspend DefaultClientWebSocketSession.() -> Unit,
    ) {
        val (host, token) = runBlocking {
            (domain ?: getCurrentDomain()) to getCurrentAccessToken()
        }

        webSocket(
            {
                this.method = HttpMethod.Get
                url {
                    protocol = URLProtocol.WSS
                    if (host != null) {
                        this.host = host
                    }
                    encodedPath = "/api/v1/streaming/"
                }
                if (token != null && !skipAuthorization) {
                    parameter("access_token", token)
                }
                httpRequestBuilder()
            },
            block
        )
    }

    private suspend fun getCurrentAccessToken() = token.getCurrentAccessToken()
    private suspend fun getCurrentDomain() = token.getCurrentDomain()
}