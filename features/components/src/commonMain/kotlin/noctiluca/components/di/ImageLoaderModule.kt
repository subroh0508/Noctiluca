package noctiluca.components.di

import io.ktor.client.*
import io.ktor.client.engine.*
import noctiluca.components.utils.ImageLoader
import org.koin.dsl.module

object ImageLoaderModule {
    operator fun invoke(engine: HttpClientEngine) = module {
        single { ImageLoader(HttpClient(engine)) }
    }
}
