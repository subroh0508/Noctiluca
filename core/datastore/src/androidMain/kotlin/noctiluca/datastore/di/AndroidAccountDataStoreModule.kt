package noctiluca.datastore.di

import android.app.Application
import androidx.datastore.core.DataStoreFactory
import kotlinx.serialization.json.Json
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.internal.*
import noctiluca.datastore.internal.SerializableAccount
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AccountDataStoreModule(json: Json) {
    single<AccountDataStore> {
        AccountDataStoreImpl(
            DataStoreFactory.create(
                createOkioStorage<List<SerializableAccount>>(
                    get<Application>(),
                    json,
                    listOf(),
                    AccountDataStore::class.simpleName ?: "",
                ),
            ),
        )
    }
}
