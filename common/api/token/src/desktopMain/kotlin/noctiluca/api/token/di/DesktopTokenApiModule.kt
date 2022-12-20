package noctiluca.api.token.di

import noctiluca.api.token.LocalTokenCache
import noctiluca.api.token.Token
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.TokenApiModule() {
    single { LocalTokenCache(get()) }
    single<Token.Cache> { get<LocalTokenCache>() }
}
