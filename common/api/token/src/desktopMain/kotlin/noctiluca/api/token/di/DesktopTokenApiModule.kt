package noctiluca.api.token.di

import noctiluca.api.token.LocalTokenCache
import noctiluca.repository.TokenCache
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.TokenApiModule() {
    single { LocalTokenCache(get()) }
    single<TokenCache> { get<LocalTokenCache>() }
}
