package noctiluca.components.utils

import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noctiluca.model.Uri
import java.io.File
import java.io.FileNotFoundException

class ImageLoader internal constructor(
    private val client: HttpClient,
) {
    companion object {
        const val RESOURCES_DIRECTORY = "src/commonMain/resources"
    }

    suspend fun loadImage(uri: Uri?): Result<ImageBitmap> {
        uri ?: return Result.failure(FileNotFoundException())

        return try {
            val imageBitmap = when {
                uri.isResourcesDirectory -> loadImageFromResources(uri.value)
                uri.isRemote -> loadImageFromRemote(uri.value)
                else -> loadImageFromLocal(uri.value)
            }

            Result.success(imageBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun loadImageFromResources(
        path: String,
    ): ImageBitmap {
        val bitmap = javaClass.getResourceAsStream(path)
            ?.readBytes()
            ?.toImageBitmap()

        return bitmap ?: throw FileNotFoundException()
    }

    private suspend fun loadImageFromRemote(
        path: String,
    ) = client.get(path).readBytes().toImageBitmap()

    private suspend fun loadImageFromLocal(
        path: String,
    ) = withContext(Dispatchers.IO) {
        File(path).readBytes().toImageBitmap()
    }

    private val Uri.isResourcesDirectory get() = value.startsWith(RESOURCES_DIRECTORY)

    private val Uri.isRemote get() = listOf(
        URLProtocol.HTTPS,
        URLProtocol.HTTP,
    ).any { value.startsWith(it.name) }
}

internal expect fun ByteArray.toImageBitmap(): ImageBitmap
