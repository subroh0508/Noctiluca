package noctiluca.datastore.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import noctiluca.datastore.AndroidAppCredentialDataStore
import noctiluca.datastore.AppCredentialDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AppCredentialDataStoreModule() {
    single<AppCredentialDataStore> {
        AndroidAppCredentialDataStore(get<Application>().appCredentialDataStore)
    }
}

private val Context.appCredentialDataStore by preferencesDataStore(
    name = AndroidAppCredentialDataStore::class.simpleName ?: "",
)
