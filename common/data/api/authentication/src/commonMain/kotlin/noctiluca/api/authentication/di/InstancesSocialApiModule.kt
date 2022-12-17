package noctiluca.api.authentication.di

import io.ktor.client.*
import noctiluca.api.authentication.InstancesSocialApi
import noctiluca.api.authentication.internal.InstancesSocialApiClient
import org.koin.dsl.module

internal expect val instancesSocialHttpClient: HttpClient

internal const val BASE_URL_INSTANCES_SOCIAL = "https://instances.social/api/1.0/instances"

object InstancesSocialApiModule {
    operator fun invoke(
        token: String,
        client: HttpClient = instancesSocialHttpClient,
    ) = module {
        single<InstancesSocialApi> { InstancesSocialApiClient(token, client) }
    }
}
