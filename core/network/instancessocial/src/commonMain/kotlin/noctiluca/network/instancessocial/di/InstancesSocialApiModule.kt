package noctiluca.network.instancessocial.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import noctiluca.core.network.instancessocial.BuildConfig
import noctiluca.network.instancessocial.InstancesSocialApi
import noctiluca.network.instancessocial.internal.InstancesSocialApiClient
import org.koin.core.module.Module

private const val HOST_INSTANCES_SOCIAL = "instances.social"

fun buildHttpClient(
    json: Json,
    engine: HttpClientEngine,
) = HttpClient(engine) {
    defaultRequest {
        url(URLProtocol.HTTPS.name, HOST_INSTANCES_SOCIAL)
        contentType(ContentType.Application.Json)
    }
    expectSuccess = true

    install(ContentNegotiation) {
        json(json)
    }

    install(Resources)
}

@Suppress("FunctionName")
fun Module.InstancesSocialApiModule(
    client: HttpClient,
    token: String = BuildConfig.SOCIAL_INSTANCES_API_TOKEN,
) {
    single<InstancesSocialApi> { InstancesSocialApiClient(token, client) }
}
