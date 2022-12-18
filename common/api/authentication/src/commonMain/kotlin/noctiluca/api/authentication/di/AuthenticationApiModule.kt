package noctiluca.api.authentication.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import noctiluca.api.authentication.AuthenticationApi
import noctiluca.api.authentication.internal.AuthenticationApiClient
import org.koin.dsl.module

fun buildHttpClient(
    json: Json,
    engine: HttpClientEngine,
) = HttpClient(engine) {
    defaultRequest {
        contentType(ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        json(json)
    }
}

object AuthenticationApiModule {
    operator fun invoke(client: HttpClient) = module {
        single<AuthenticationApi> { AuthenticationApiClient(client) }
    }
}
