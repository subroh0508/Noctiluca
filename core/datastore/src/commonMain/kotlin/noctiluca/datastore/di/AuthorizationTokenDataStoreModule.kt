package noctiluca.datastore.di

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import kotlinx.serialization.json.Json
import noctiluca.datastore.internal.Token
import org.koin.core.module.Module

@Suppress("FunctionName")
internal expect fun Module.AuthorizationTokenDataStoreModule(json: Json)

internal expect fun createAuthorizationTokenDataStore(
    json: Json,
    migrations: List<DataMigration<List<Token.Json>>> = listOf(),
    context: Any? = null,
): DataStore<List<Token.Json>>
