package noctiluca.datastore.di

import android.app.Application
import androidx.datastore.core.DataStoreFactory
import kotlinx.serialization.json.Json
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.datastore.internal.*
import noctiluca.datastore.internal.Token
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AuthorizationTokenDataStoreModule(json: Json) {
    single<AuthorizationTokenDataStore> {
        AuthorizationTokenDataStoreImpl(
            DataStoreFactory.create(
                createOkioStorage<List<Token.Json>>(
                    get<Application>(),
                    json,
                    listOf(),
                    AuthorizationTokenDataStore::class.simpleName ?: "",
                ),
            ),
        )
    }
}
