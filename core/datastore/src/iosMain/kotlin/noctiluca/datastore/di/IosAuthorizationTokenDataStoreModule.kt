package noctiluca.datastore.di

import androidx.datastore.core.DataStoreFactory
import kotlinx.serialization.json.Json
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.datastore.internal.*
import noctiluca.datastore.internal.createOkioStorage
import okio.FileSystem
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AuthorizationTokenDataStoreModule(json: Json) {
    single<AuthorizationTokenDataStore> {
        AuthorizationTokenDataStoreImpl(
            DataStoreFactory.create(
                createOkioStorage(
                    json,
                    listOf(),
                    AuthorizationTokenDataStore::class.simpleName ?: "",
                    FileSystem.SYSTEM,
                ),
            )
        )
    }
}
