package noctiluca.authentication.infra.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.impl.TokenRepositoryImpl
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import noctiluca.repository.TokenProvider
import org.koin.core.module.Module

private val Context.appCredentialDataStore by preferencesDataStore(name = AppCredentialCache::class.simpleName ?: "")

@Suppress("FunctionName")
actual fun Module.AuthenticationRepositoriesModule() {
    single { AppCredentialCache(get<Application>().appCredentialDataStore) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get(), get()) }
    single<TokenProvider> { get<TokenRepository>() }
}
