package noctiluca.api.authentication.di

import io.ktor.client.*
import noctiluca.api.authentication.AuthenticationApi
import noctiluca.api.authentication.internal.AuthenticationApiClient
import org.koin.dsl.module

internal expect val httpClient: HttpClient

object AuthenticationApiModule {
    operator fun invoke(client: HttpClient = httpClient) = module {
        single<AuthenticationApi> { AuthenticationApiClient(client) }
    }
}
