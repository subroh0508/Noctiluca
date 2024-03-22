package noctiluca.datastore.di

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import kotlinx.serialization.json.Json
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.datastore.internal.*
import noctiluca.datastore.internal.JsonSerializer
import noctiluca.datastore.internal.Token
import noctiluca.datastore.internal.dataStoreFile
import noctiluca.datastore.internal.getJsonDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AuthorizationTokenDataStoreModule(json: Json) {
    single<AuthorizationTokenDataStore> {
        AuthorizationTokenDataStoreImpl(
            createAuthorizationTokenDataStore(json, context = null),
        )
    }
}

internal actual fun createAuthorizationTokenDataStore(
    json: Json,
    migrations: List<DataMigration<List<Token.Json>>>,
    context: Any?,
): DataStore<List<Token.Json>> = getJsonDataStore(
    JsonSerializer(json, listOf()),
    dataStoreFile(fileName = AuthorizationTokenDataStore::class.simpleName ?: ""),
)
