package app.noctiluca

import android.app.Application
import app.noctiluca.shared.AppEntryPoint
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.option.androidContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cache.HttpCache
import okhttp3.logging.HttpLoggingInterceptor
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext

class NoctilucaApplication : Application() {
    private val httpClientEngine by lazy {
        OkHttp.create {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
    }

    override fun onCreate() {
        super.onCreate()

        AppEntryPoint.init(
            httpClientEngine,
            buildImageLoader(),
        ) {
            androidContext(this@NoctilucaApplication)
        }
    }

    private fun buildImageLoader() = ImageLoader {
        options { androidContext(this@NoctilucaApplication) }
        components {
            setupDefaultComponents {
                HttpClient(httpClientEngine) {
                    install(HttpCache)
                }
            }
        }
        interceptor {
            // cache 100 success image result, without bitmap
            defaultImageResultMemoryCache()
            bitmapMemoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(this@NoctilucaApplication, 0.25)
            }
            diskCacheConfig {
                directory(cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}
