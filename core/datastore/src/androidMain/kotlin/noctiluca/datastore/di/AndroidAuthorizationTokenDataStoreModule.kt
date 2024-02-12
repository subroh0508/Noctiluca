package noctiluca.datastore.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.datastore.AndroidAuthorizationTokenDataStore
import noctiluca.datastore.AuthorizationTokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AuthorizationTokenDataStoreModule(json: Json) {
    single<AuthorizationTokenDataStore> { AndroidAuthorizationTokenDataStore(get<Application>(), json) }
    // single<TokenCache> { get<LocalTokenCache>() }
}
