package noctiluca.authentication.infra.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import org.koin.core.module.Module

private val Context.appCredentialDataStore by preferencesDataStore(
    name = AppCredentialCache::class.simpleName ?: ""
)

@Suppress("FunctionName")
actual fun Module.AppCredentialCacheModule() {
    single { AppCredentialCache(get<Application>().appCredentialDataStore) }
}
