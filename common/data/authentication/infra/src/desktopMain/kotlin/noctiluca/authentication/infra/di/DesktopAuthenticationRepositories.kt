package noctiluca.authentication.infra.di

import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.impl.TokenRepositoryImpl
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import noctiluca.authentication.infra.repository.local.TokenCache
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
actual fun Module.AuthenticationRepositoriesModule() {
    single { AppCredentialCache(get()) }
    single { TokenCache(get()) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get(), get()) }
}
