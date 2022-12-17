package noctiluca.api.instancessocial.di

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import okhttp3.logging.HttpLoggingInterceptor

internal actual val httpClient: HttpClient get() = HttpClient(OkHttp) {
    engine {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)
    }

    defaultRequest {
        url(BASE_URL_INSTANCES_SOCIAL)
        contentType(ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        json()
    }
}
