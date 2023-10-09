package noctiluca.api.authentication.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import noctiluca.api.authentication.AuthenticationApi
import noctiluca.api.authentication.internal.AuthenticationApiClient
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
fun Module.AuthenticationApiModule(client: HttpClient) {
    single<AuthenticationApi> { AuthenticationApiClient(client) }
}
