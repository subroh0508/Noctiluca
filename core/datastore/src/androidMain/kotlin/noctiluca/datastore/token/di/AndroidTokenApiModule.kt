package noctiluca.datastore.token.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.datastore.token.LocalTokenCache
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.TokenApiModule(json: Json) {
    single { LocalTokenCache(get<Application>(), json) }
    // single<TokenCache> { get<LocalTokenCache>() }
}
