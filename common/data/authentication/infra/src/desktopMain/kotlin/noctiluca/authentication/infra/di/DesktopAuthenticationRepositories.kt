package noctiluca.authentication.infra.di

import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.impl.TokenRepositoryImpl
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
actual fun Module.AuthenticationRepositoriesModule() {
    single { AppCredentialCache(get()) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get(), get()) }
}
