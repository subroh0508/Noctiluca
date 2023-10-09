package noctiluca.datastore.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.datastore.TokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.TokenDataStoreModule(json: Json) {
    single { TokenDataStore(get<Application>(), json) }
    // single<TokenCache> { get<LocalTokenCache>() }
}
