package noctiluca.api.token.internal

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object TokenJsonSerializer : Serializer<List<Token.Json>> {
    override val defaultValue: List<Token.Json> = listOf()

    override suspend fun readFrom(input: InputStream) = try {
        val bytes = input.readBytes()

        Json.decodeFromString<Array<Token.Json>>(bytes.decodeToString()).toList()
    } catch (@Suppress("SwallowedException") e: SerializationException) {
        defaultValue
    }

    override suspend fun writeTo(t: List<Token.Json>, output: OutputStream) {
        val json = Json.encodeToString(t.toTypedArray())

        withContext(Dispatchers.IO) {
            output.write(json.encodeToByteArray())
        }
    }
}
