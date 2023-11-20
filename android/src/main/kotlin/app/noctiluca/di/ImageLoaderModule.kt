package app.noctiluca.di

import android.app.Application
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import com.seiko.imageloader.option.androidContext
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.cache.*
import okio.Path.Companion.toOkioPath
import org.koin.dsl.module

object ImageLoaderModule {
    operator fun invoke(
        application: Application,
        engine: HttpClientEngine,
    ) = module {
        single { buildImageLoader(application, engine) }
    }

    private fun buildImageLoader(
        application: Application,
        engine: HttpClientEngine,
    ) = ImageLoader {
        options { androidContext(application) }
        components {
            setupDefaultComponents {
                HttpClient(engine) {
                    install(HttpCache)
                }
            }
        }
        interceptor {
            // cache 100 success image result, without bitmap
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(application, 0.25)
            }
            diskCacheConfig {
                directory(application.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}
