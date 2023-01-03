package noctiluca.api.mastodon.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.MastodonV1Api
import noctiluca.api.mastodon.internal.MastodonV1ApiClient
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
    single<MastodonV1Api> { MastodonV1ApiClient(get(), client) }
}
