package noctiluca.datastore.di

import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.DesktopAppCredentialDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AppCredentialDataStoreModule() {
    single<AppCredentialDataStore> { DesktopAppCredentialDataStore() }
}
