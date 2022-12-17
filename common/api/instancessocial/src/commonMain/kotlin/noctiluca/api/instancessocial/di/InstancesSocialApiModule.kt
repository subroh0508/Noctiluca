package noctiluca.api.instancessocial.di

import io.ktor.client.*
import noctiluca.common.api.instancessocial.BuildConfig
import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.api.instancessocial.internal.InstancesSocialApiClient
import org.koin.dsl.module

internal expect val httpClient: HttpClient

internal const val HOST_INSTANCES_SOCIAL = "instances.social"

object InstancesSocialApiModule {
    operator fun invoke(
        token: String = BuildConfig.SOCIAL_INSTANCES_API_TOKEN,
        client: HttpClient = httpClient,
    ) = module {
        single<InstancesSocialApi> { InstancesSocialApiClient(token, client) }
    }
}
