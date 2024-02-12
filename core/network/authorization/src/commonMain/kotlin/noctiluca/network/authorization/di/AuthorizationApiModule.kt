package noctiluca.network.authorization.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import noctiluca.network.authorization.AuthorizationApi
import noctiluca.network.authorization.internal.AuthorizationApiClient
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

@Suppress("FunctionName")
fun Module.AuthorizationApiModule(client: HttpClient) {
    single<AuthorizationApi> { AuthorizationApiClient(client) }
}
