package noctiluca.datastore.internal

import androidx.datastore.core.okio.OkioStorage
import kotlinx.serialization.json.Json

internal expect inline fun <reified T : Any?> createOkioStorage(
    json: Json,
    defaultValue: T,
    fileName: String,
): OkioStorage<T>
