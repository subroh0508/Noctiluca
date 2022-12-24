package noctiluca.api.token.di

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import noctiluca.api.token.LocalTokenCache
import noctiluca.api.token.internal.Token
import noctiluca.api.token.internal.TokenJsonSerializer
import noctiluca.repository.TokenCache
import org.koin.core.module.Module

private val Context.tokenDataStore by dataStore(
    fileName = Token.Json::class.simpleName ?: "",
    serializer = TokenJsonSerializer,
)

@Suppress("FunctionName")
actual fun Module.TokenApiModule() {
    single { LocalTokenCache(get<Application>().tokenDataStore) }
    single<TokenCache> { get<LocalTokenCache>() }
}
