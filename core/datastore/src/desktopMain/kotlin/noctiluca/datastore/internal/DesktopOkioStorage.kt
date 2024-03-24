package noctiluca.datastore.internal

import androidx.datastore.core.okio.OkioStorage
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import java.io.File

internal actual inline fun <reified T : Any?> createOkioStorage(
    json: Json,
    defaultValue: T,
    fileName: String,
    fileSystem: FileSystem,
): OkioStorage<T> = OkioStorage(
    fileSystem,
    OkioJsonSerializer(json, defaultValue),
) { File(fileName).absoluteFile.toOkioPath() }
