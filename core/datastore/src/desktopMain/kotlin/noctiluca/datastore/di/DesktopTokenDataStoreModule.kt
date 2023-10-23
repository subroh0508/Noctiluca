package noctiluca.datastore.di

import kotlinx.serialization.json.Json
import noctiluca.datastore.DesktopTokenDataStore
import noctiluca.datastore.JsonPreferences
import noctiluca.datastore.TokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.TokenDataStoreModule(json: Json) {
    single<TokenDataStore> { DesktopTokenDataStore(JsonPreferences(json, listOf(), get())) }
}
