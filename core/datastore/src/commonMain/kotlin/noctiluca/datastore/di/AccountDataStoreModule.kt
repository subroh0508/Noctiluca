package noctiluca.datastore.di

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import kotlinx.serialization.json.Json
import noctiluca.datastore.internal.SerializableAccount
import org.koin.core.module.Module

@Suppress("FunctionName")
internal expect fun Module.AccountDataStoreModule(json: Json)

internal expect fun createAccountDataStore(
    json: Json,
    migrations: List<DataMigration<List<SerializableAccount>>> = listOf(),
    context: Any? = null,
): DataStore<List<SerializableAccount>>
