package noctiluca.api.mastodon.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.MastodonApi
import noctiluca.api.mastodon.internal.MastodonApiClient
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
}

@Suppress("FunctionName")
fun Module.MastodonApiModule(
    client: HttpClient,
) {
    single<MastodonApi> { MastodonApiClient(get(), client) }
}
