package noctiluca.datastore.di

import androidx.datastore.core.DataStoreFactory
import kotlinx.serialization.json.Json
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.internal.AccountDataStoreImpl
import noctiluca.datastore.internal.createOkioStorage
import okio.FileSystem
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AccountDataStoreModule(json: Json) {
    single<AccountDataStore> {
        AccountDataStoreImpl(
            DataStoreFactory.create(
                createOkioStorage(
                    json,
                    listOf(),
                    AccountDataStore::class.simpleName ?: "",
                    FileSystem.SYSTEM,
                ),
            ),
        )
    }
}
