package noctiluca.datastore.internal

import androidx.datastore.core.okio.OkioStorage
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath

internal actual inline fun <reified T : Any?> createOkioStorage(
    json: Json,
    defaultValue: T,
    fileName: String,
): OkioStorage<T> = OkioStorage(
    FileSystem.SYSTEM,
    OkioJsonSerializer(json, defaultValue),
) { fileName.toPath() }
