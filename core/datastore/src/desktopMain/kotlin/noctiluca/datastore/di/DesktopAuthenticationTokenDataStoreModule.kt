package noctiluca.datastore.di

import kotlinx.serialization.json.Json
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.datastore.DesktopAuthenticationTokenDataStore
import noctiluca.datastore.JsonPreferences
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AuthenticationTokenDataStoreModule(json: Json) {
    single<AuthenticationTokenDataStore> { DesktopAuthenticationTokenDataStore(JsonPreferences(json, listOf(), get())) }
}
