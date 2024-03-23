import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.cache.HttpCache
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal fun buildImageLoader(
    httpClientEngine: HttpClientEngine,
) = ImageLoader {
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
            maxSize(32 * 1024 * 1024)
        }
        // cache 50 image
        imageMemoryCacheConfig {
            maxSize(50)
        }
        // cache 50 painter
        painterMemoryCacheConfig {
            maxSize(50)
        }
        diskCacheConfig {
            directory(getCacheDir().toPath().resolve("image_cache"))
            maxSizeBytes(512L * 1024 * 1024) // 512MB
        }
    }
}

private fun getCacheDir(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true,
    ).first() as String
}
