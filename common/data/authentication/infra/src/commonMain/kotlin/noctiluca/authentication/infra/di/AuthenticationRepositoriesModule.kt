package noctiluca.authentication.infra.di

import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.impl.TokenRepositoryImpl
import noctiluca.repository.TokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AuthenticationRepositoriesModule() {
    single<TokenRepository> { TokenRepositoryImpl(get(), get()) }
    single<TokenProvider> { get<TokenRepository>() }
}
