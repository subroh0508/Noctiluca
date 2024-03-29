package noctiluca.datastore.internal

import android.content.Context
import androidx.datastore.core.okio.OkioStorage
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import java.io.File

internal inline fun <reified T : Any?> createOkioStorage(
    context: Context?,
    json: Json,
    defaultValue: T,
    fileName: String,
) = context?.let {
    createOkioStorage(
        json,
        defaultValue,
        File(it.filesDir, "datastore/$fileName").absolutePath,
        FileSystem.SYSTEM,
    )
} ?: throw IllegalArgumentException("Context is required")

internal actual inline fun <reified T : Any?> createOkioStorage(
    json: Json,
    defaultValue: T,
    fileName: String,
    fileSystem: FileSystem,
): OkioStorage<T> = OkioStorage(
    fileSystem,
    OkioJsonSerializer(json, defaultValue),
) { File(fileName).absoluteFile.toOkioPath() }
