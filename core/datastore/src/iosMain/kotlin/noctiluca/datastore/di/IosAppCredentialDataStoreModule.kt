package noctiluca.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.internal.AppCredentialDataStoreImpl
import noctiluca.datastore.internal.createDocumentDirectoryPath
import okio.Path.Companion.toPath
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AppCredentialDataStoreModule() {
    single<AppCredentialDataStore> {
        AppCredentialDataStoreImpl(
            PreferenceDataStoreFactory.createWithPath {
                createDocumentDirectoryPath(
                    preferencesFileName(AppCredentialDataStore::class.simpleName ?: ""),
                ).toPath()
            },
        )
    }
}
