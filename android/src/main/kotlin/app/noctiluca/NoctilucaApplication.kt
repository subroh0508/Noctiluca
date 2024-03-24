package app.noctiluca

import android.app.Application
import app.noctiluca.shared.AppEntryPoint
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import com.seiko.imageloader.option.androidContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cache.HttpCache
import okhttp3.logging.HttpLoggingInterceptor
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext

class NoctilucaApplication : Application() {
    companion object {
        private const val MAX_BITMAP_MEMORY_CACHE_SIZE_PERCENT = 0.25
        private const val MAX_IMAGE_MEMORY_CACHE_SIZE = 50
        private const val MAX_PAINTER_MEMORY_CACHE_SIZE = 50
        private const val MAX_DISK_CACHE_SIZE = 512L * 1024 * 1024
    }

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
            bitmapMemoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(this@NoctilucaApplication, MAX_BITMAP_MEMORY_CACHE_SIZE_PERCENT)
            }
            // cache 50 image
            imageMemoryCacheConfig {
                maxSize(MAX_IMAGE_MEMORY_CACHE_SIZE)
            }
            // cache 50 painter
            painterMemoryCacheConfig {
                maxSize(MAX_PAINTER_MEMORY_CACHE_SIZE)
            }
            diskCacheConfig {
                directory(cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(MAX_DISK_CACHE_SIZE) // 512MB
            }
        }
    }
}
