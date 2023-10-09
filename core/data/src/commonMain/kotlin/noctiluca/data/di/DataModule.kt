package noctiluca.data.di

import noctiluca.data.authentication.AppCredentialRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.authentication.impl.AppCredentialRepositoryImpl
import noctiluca.data.authentication.impl.AuthorizedUserRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataModule() {
    single<AppCredentialRepository> { AppCredentialRepositoryImpl(get(), get()) }
    single<AuthorizedUserRepository> { AuthorizedUserRepositoryImpl(get(), get(), get()) }
}
