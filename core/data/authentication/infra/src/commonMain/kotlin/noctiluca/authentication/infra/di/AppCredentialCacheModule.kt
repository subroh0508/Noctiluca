package noctiluca.authentication.infra.di

import noctiluca.authentication.infra.repository.local.LocalTokenRepository
import noctiluca.authentication.infra.repository.local.impl.LocalTokenRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
expect fun Module.AppCredentialCacheModule()

@Suppress("FunctionName")
internal fun Module.LocalTokenRepositoriesModule() {
    AppCredentialCacheModule()
    single<LocalTokenRepository> { LocalTokenRepositoryImpl(get(), get()) }
}
