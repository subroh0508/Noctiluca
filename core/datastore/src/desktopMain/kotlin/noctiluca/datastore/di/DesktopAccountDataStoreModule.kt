package noctiluca.datastore.di

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import kotlinx.serialization.json.Json
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.internal.AccountDataStoreImpl
import noctiluca.datastore.internal.JsonSerializer
import noctiluca.datastore.internal.SerializableAccount
import noctiluca.datastore.internal.dataStoreFile
import noctiluca.datastore.internal.getJsonDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AccountDataStoreModule(json: Json) {
    single<AccountDataStore> {
        AccountDataStoreImpl(
            createAccountDataStore(json, context = null),
        )
    }
}

internal actual fun createAccountDataStore(
    json: Json,
    migrations: List<DataMigration<List<SerializableAccount>>>,
    context: Any?,
): DataStore<List<SerializableAccount>> = getJsonDataStore(
    JsonSerializer(json, listOf()),
    dataStoreFile(fileName = AccountDataStore::class.simpleName ?: ""),
)
