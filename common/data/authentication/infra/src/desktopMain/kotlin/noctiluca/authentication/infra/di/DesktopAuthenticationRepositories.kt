package noctiluca.authentication.infra.di

import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.impl.TokenRepositoryImpl
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import noctiluca.repository.TokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AuthenticationRepositoriesModule() {
    single { AppCredentialCache(get()) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get(), get()) }
    single<TokenProvider> { get<TokenRepository>() }
}
