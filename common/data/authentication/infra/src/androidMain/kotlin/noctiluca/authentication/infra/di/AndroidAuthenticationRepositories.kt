package noctiluca.authentication.infra.di

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import noctiluca.authentication.infra.internal.CachedTokenSerializer
import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.impl.TokenRepositoryImpl
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import noctiluca.authentication.infra.repository.local.TokenCache
import org.koin.core.module.Module
import org.koin.dsl.module

private val Context.appCredentialDataStore by preferencesDataStore(name = AppCredentialCache::class.simpleName ?: "")
private val Context.tokenDataStore by dataStore(
    fileName = TokenCache::class.simpleName ?: "",
    serializer = CachedTokenSerializer,
)

@Suppress("FunctionName")
actual fun Module.AuthenticationRepositoriesModule() {
    single { AppCredentialCache(get<Application>().appCredentialDataStore) }
    single { TokenCache(get<Application>().tokenDataStore) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get(), get()) }
}
