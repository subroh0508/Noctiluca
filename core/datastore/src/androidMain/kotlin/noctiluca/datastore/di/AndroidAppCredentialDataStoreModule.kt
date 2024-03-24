package noctiluca.datastore.di

import android.app.Application
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
                    get<Application>().filesDir,
                    "datastore/${preferencesFileName(AppCredentialDataStore::class.simpleName ?: "")}",
                ).path.toPath()
            },
        )
    }
}
