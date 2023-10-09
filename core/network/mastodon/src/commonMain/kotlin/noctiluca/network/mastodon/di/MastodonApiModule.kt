package noctiluca.network.mastodon.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.MastodonApiV2
import noctiluca.network.mastodon.MastodonStream
import noctiluca.network.mastodon.internal.MastodonApiV1Client
import noctiluca.network.mastodon.internal.MastodonApiV2Client
import noctiluca.network.mastodon.internal.MastodonStreamClient
import org.koin.core.module.Module

fun buildHttpClient(
    json: Json,
    engine: HttpClientEngine,
) = HttpClient(engine) {
    defaultRequest {
        url { protocol = URLProtocol.HTTPS }
        contentType(ContentType.Application.Json)
    }
    expectSuccess = true

    install(ContentNegotiation) {
        json(json)
    }

    install(Resources)
}

fun buildWebSocketClient(
    engine: HttpClientEngine,
) = HttpClient(engine) {
    defaultRequest {
        url { protocol = URLProtocol.WSS }
    }

    install(WebSockets)
}

@Suppress("FunctionName")
fun Module.MastodonApiModule(
    client: HttpClient,
    webSocket: HttpClient,
    json: Json,
) {
    single<noctiluca.network.mastodon.MastodonApiV1> { MastodonApiV1Client(get(), client) }
    single<noctiluca.network.mastodon.MastodonApiV2> { MastodonApiV2Client(get(), client) }
    single<noctiluca.network.mastodon.MastodonStream> { MastodonStreamClient(get(), webSocket, json) }
}
