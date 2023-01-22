package noctiluca.api.mastodon.internal

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
import noctiluca.api.mastodon.MastodonStream
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.api.mastodon.json.streaming.Event
import noctiluca.api.mastodon.json.streaming.StreamEventJson
import noctiluca.repository.TokenCache

internal class MastodonStreamClient(
    private val token: TokenCache,
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
    ): Flow<StreamEventJson> = flow {
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
        StreamEventJson(
            raw.stream,
            raw.event,
            raw.buildPayload(),
        )
    }

    private fun StreamEventRawJson.buildPayload(): StreamEventJson.Payload? {
        payload ?: return null

        return when (Event.findEvent(event)) {
            Event.UPDATE, Event.STATUS_UPDATE -> StreamEventJson.Payload.Updated(
                json.decodeFromString(StatusJson.serializer(), payload),
            )
            Event.DELETE -> StreamEventJson.Payload.Deleted(payload)
            else -> null
        }
    }
}