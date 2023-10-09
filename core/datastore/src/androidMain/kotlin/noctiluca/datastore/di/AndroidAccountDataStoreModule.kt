package noctiluca.datastore.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.datastore.AccountDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AccountDataStoreModule(json: Json) {
    single { AccountDataStore(get<Application>(), json) }
}
