package noctiluca.datastore

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
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

internal fun <T : Any?> Context.getJsonDataStore(
    serializer: JsonSerializer<T>,
    fileName: String,
) = DataStoreFactory.create(
    serializer,
    produceFile = { dataStoreFile(fileName) },
)
