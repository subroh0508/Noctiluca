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

private const val MAX_BITMAP_MEMORY_CACHE_SIZE = 32 * 1024 * 1024
private const val MAX_IMAGE_MEMORY_CACHE_SIZE = 50
private const val MAX_PAINTER_MEMORY_CACHE_SIZE = 50
private const val MAX_DISK_CACHE_SIZE = 512L * 1024 * 1024

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
            maxSize(MAX_BITMAP_MEMORY_CACHE_SIZE)
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
            directory(getCacheDir().toPath().resolve("image_cache"))
            maxSizeBytes(MAX_DISK_CACHE_SIZE) // 512MB
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
