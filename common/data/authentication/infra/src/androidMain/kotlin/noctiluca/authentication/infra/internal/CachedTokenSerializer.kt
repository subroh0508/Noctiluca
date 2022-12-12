package noctiluca.authentication.infra.internal

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object CachedTokenSerializer : Serializer<List<CachedToken>> {
    override val defaultValue: List<CachedToken> = listOf()

    override suspend fun readFrom(input: InputStream) = try {
        val bytes = input.readBytes()

        Json.decodeFromString<Array<CachedToken>>(bytes.decodeToString()).toList()
    } catch (e: SerializationException) {
        defaultValue
    }

    override suspend fun writeTo(t: List<CachedToken>, output: OutputStream) {
        val json = Json.encodeToString(t.toTypedArray())

        withContext(Dispatchers.IO) {
            output.write(json.encodeToByteArray())
        }
    }
}
