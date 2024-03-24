package noctiluca.datastore.internal

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okio.BufferedSink
import okio.BufferedSource

internal class OkioJsonSerializer<T : Any?>(
    private val json: Json,
    private val serializer: KSerializer<T>,
    override val defaultValue: T,
) : OkioSerializer<T> {
    companion object {
        inline operator fun <reified T : Any?> invoke(
            json: Json,
            defaultValue: T,
        ) = OkioJsonSerializer(json, serializer(), defaultValue)
    }

    override suspend fun readFrom(source: BufferedSource): T = try {
        val bytes = source.readByteArray()

        json.decodeFromString(serializer, bytes.decodeToString())
    } catch (@Suppress("SwallowedException") e: SerializationException) {
        defaultValue
    }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        val jsonString = t?.let { json.encodeToString(serializer, it) } ?: ""
        sink.write(jsonString.encodeToByteArray())
    }
}
