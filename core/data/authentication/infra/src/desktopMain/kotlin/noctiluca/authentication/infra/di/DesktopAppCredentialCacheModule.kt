package noctiluca.authentication.infra.di

import noctiluca.authentication.infra.repository.local.AppCredentialCache
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AppCredentialCacheModule() {
    single { AppCredentialCache(get()) }
}
