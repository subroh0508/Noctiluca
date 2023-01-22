package noctiluca.api.mastodon.di

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
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.internal.MastodonApiV1Client
import org.koin.core.module.Module

fun buildHttpClient(
    json: Json,
    engine: HttpClientEngine,
) = HttpClient(engine) {
    defaultRequest {
        url { protocol = URLProtocol.HTTPS }
        contentType(ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        json(json)
    }

    install(WebSockets)
    install(Resources)
}

@Suppress("FunctionName")
fun Module.MastodonApiModule(
    client: HttpClient,
    json: Json,
) {
    single<MastodonApiV1> { MastodonApiV1Client(get(), client, json) }
}
