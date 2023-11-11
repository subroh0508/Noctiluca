package noctiluca.network.mastodon.internal

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import noctiluca.network.mastodon.AuthenticationTokenProvider
import noctiluca.network.mastodon.MastodonStream
import noctiluca.network.mastodon.data.status.NetworkStatus
import noctiluca.network.mastodon.data.streaming.Event
import noctiluca.network.mastodon.data.streaming.NetworkStreamEvent

internal class MastodonStreamClient(
    private val token: AuthenticationTokenProvider,
    private val client: HttpClient,
    private val json: Json,
) : MastodonStream {
    companion object {
        private const val ENDPOINT = "/api/v1/streaming/"
    }

    override suspend fun streaming(
        stream: String,
        type: String,
        listId: String?,
        tag: String?,
    ): Flow<NetworkStreamEvent> = flow {
        client.websocket(
            httpRequestBuilder = {
                parameter("type", type)
                parameter("stream", stream)
                parameter("list", listId)
                parameter("tag", tag)
            },
        ) {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()

                    println("streaming: $text")
                    emit(decode(text))
                }
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
                    encodedPath = ENDPOINT
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

    @Serializable
    private data class StreamEventRawJson(
        val stream: List<String>,
        val event: String,
        val payload: String?,
    )

    private fun decode(text: String) = json.decodeFromString(
        StreamEventRawJson.serializer(),
        text,
    ).let { raw ->
        NetworkStreamEvent(
            raw.stream,
            raw.event,
            raw.buildPayload(),
        )
    }

    private fun StreamEventRawJson.buildPayload(): NetworkStreamEvent.Payload? {
        payload ?: return null

        return when (Event.findEvent(event)) {
            Event.UPDATE, Event.STATUS_UPDATE -> NetworkStreamEvent.Payload.Updated(
                json.decodeFromString(NetworkStatus.serializer(), payload),
            )

            Event.DELETE -> NetworkStreamEvent.Payload.Deleted(payload)
            else -> null
        }
    }
}
