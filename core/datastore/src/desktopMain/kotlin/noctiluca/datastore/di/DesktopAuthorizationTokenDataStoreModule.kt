package noctiluca.datastore.di

import kotlinx.serialization.json.Json
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.datastore.DesktopAuthorizationTokenDataStore
import noctiluca.datastore.JsonPreferences
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AuthorizationTokenDataStoreModule(json: Json) {
    single<AuthorizationTokenDataStore> { DesktopAuthorizationTokenDataStore(JsonPreferences(json, listOf(), get())) }
}
