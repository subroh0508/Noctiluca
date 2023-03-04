package noctiluca.account.infra.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import noctiluca.account.infra.repository.local.LocalAccountCredentialCache
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import org.koin.core.module.Module
import java.io.InputStream
import java.io.OutputStream

private fun Context.getAccountCredentialDataStore(json: Json) = DataStoreFactory.create(
    AccountCredentialJsonSerializer(json),
    produceFile = { dataStoreFile(LocalAccountCredentialCache::class.simpleName ?: "") },
)

private class AccountCredentialJsonSerializer(
    private val json: Json,
) : Serializer<List<AccountCredentialJson>> {
    override val defaultValue: List<AccountCredentialJson> = listOf()

    override suspend fun readFrom(input: InputStream) = try {
        val bytes = input.readBytes()

        json.decodeFromString<Array<AccountCredentialJson>>(bytes.decodeToString()).toList()
    } catch (@Suppress("SwallowedException") e: SerializationException) {
        defaultValue
    }

    override suspend fun writeTo(t: List<AccountCredentialJson>, output: OutputStream) {
        val jsonString = json.encodeToString(t.toTypedArray())
        withContext(Dispatchers.IO) {
            output.write(jsonString.encodeToByteArray())
        }
    }
}

@Suppress("FunctionName")
actual fun Module.AccountCredentialCacheModule(json: Json) {
    single { LocalAccountCredentialCache(get<Application>().getAccountCredentialDataStore(json)) }
}
