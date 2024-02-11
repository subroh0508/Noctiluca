package noctiluca.data.di

import noctiluca.data.authentication.AuthenticationRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.authentication.impl.AuthenticationRepositoryImpl
import noctiluca.data.authentication.impl.AuthorizedUserRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataAuthenticationModule() {
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
    single<AuthorizedUserRepository> { AuthorizedUserRepositoryImpl(get()) }
}
