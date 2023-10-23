package noctiluca.datastore.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.AndroidAccountDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AccountDataStoreModule(json: Json) {
    single<AccountDataStore> { AndroidAccountDataStore(get<Application>(), json) }
}
