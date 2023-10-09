package noctiluca.api.token.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.api.token.LocalTokenCache
import noctiluca.repository.TokenCache
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.TokenApiModule(json: Json) {
    single { LocalTokenCache(get<Application>(), json) }
    single<TokenCache> { get<LocalTokenCache>() }
}
