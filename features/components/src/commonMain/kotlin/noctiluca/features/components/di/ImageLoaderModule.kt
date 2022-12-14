package noctiluca.features.components.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.cache.*
import noctiluca.features.components.utils.ImageLoader
import org.koin.dsl.module

object ImageLoaderModule {
    operator fun invoke(engine: HttpClientEngine) = module {
        val client = HttpClient(engine) {
            install(HttpCache)
        }

        single { ImageLoader(client) }
    }
}
