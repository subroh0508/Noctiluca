package noctiluca.datastore.di

import android.app.Application
import android.content.Context
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
            createAuthorizationTokenDataStore(
                json,
                context = get<Application>(),
            ),
        )
    }
    // single<TokenCache> { get<LocalTokenCache>() }
}

internal actual fun createAuthorizationTokenDataStore(
    json: Json,
    migrations: List<DataMigration<List<Token.Json>>>,
    context: Any?,
): DataStore<List<Token.Json>> = getJsonDataStore(
    JsonSerializer(json, listOf()),
    (context as? Context)?.let {
        dataStoreFile(
            dir = it.filesDir,
            fileName = AuthorizationTokenDataStore::class.simpleName ?: "",
        )
    } ?: throw IllegalArgumentException("Context is required")
)
