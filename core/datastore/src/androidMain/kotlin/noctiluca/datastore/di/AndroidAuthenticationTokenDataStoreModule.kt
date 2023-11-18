package noctiluca.datastore.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.datastore.AndroidAuthenticationTokenDataStore
import noctiluca.datastore.AuthenticationTokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AuthenticationTokenDataStoreModule(json: Json) {
    single<AuthenticationTokenDataStore> { AndroidAuthenticationTokenDataStore(get<Application>(), json) }
    // single<TokenCache> { get<LocalTokenCache>() }
}
