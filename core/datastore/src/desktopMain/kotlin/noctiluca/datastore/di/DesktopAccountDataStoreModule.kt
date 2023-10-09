package noctiluca.datastore.di

import kotlinx.serialization.json.Json
import noctiluca.datastore.AccountDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AccountDataStoreModule(json: Json) {
    single { AccountDataStore(json) }
}
