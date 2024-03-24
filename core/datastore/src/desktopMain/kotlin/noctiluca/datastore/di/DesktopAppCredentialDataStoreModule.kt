package noctiluca.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.internal.AppCredentialDataStoreImpl
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import java.io.File

@Suppress("FunctionName")
actual fun Module.AppCredentialDataStoreModule() {
    single<AppCredentialDataStore> {
        AppCredentialDataStoreImpl(
            PreferenceDataStoreFactory.createWithPath {
                File(
                    preferencesFileName(AppCredentialDataStore::class.simpleName ?: ""),
                ).path.toPath()
            },
        )
    }
}
