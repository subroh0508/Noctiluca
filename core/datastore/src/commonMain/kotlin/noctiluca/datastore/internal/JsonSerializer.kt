package noctiluca.datastore.internal

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.File
import java.io.InputStream
import java.io.OutputStream

internal class JsonSerializer<T : Any?>(
    private val json: Json,
    private val serializer: KSerializer<T>,
    override val defaultValue: T,
) : Serializer<T> {
    companion object {
        inline operator fun <reified T : Any?> invoke(
            json: Json,
            defaultValue: T,
        ) = JsonSerializer(json, serializer(), defaultValue)
    }

    override suspend fun readFrom(input: InputStream) = try {
        val bytes = input.readBytes()

        json.decodeFromString(serializer, bytes.decodeToString())
    } catch (@Suppress("SwallowedException") e: SerializationException) {
        defaultValue
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val jsonString = t?.let { json.encodeToString(serializer, it) } ?: ""
        withContext(Dispatchers.IO) {
            output.write(jsonString.encodeToByteArray())
        }
    }
}

internal fun <T : Any?> getJsonDataStore(
    serializer: Serializer<T>,
    file: File,
) = DataStoreFactory.create(
    serializer,
    produceFile = { file },
)
