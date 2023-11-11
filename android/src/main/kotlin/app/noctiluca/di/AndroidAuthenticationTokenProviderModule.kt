package app.noctiluca.di

import noctiluca.datastore.TokenDataStore
import noctiluca.network.mastodon.AuthenticationTokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun Module.AndroidAuthenticationTokenProviderModule() {
    single<AuthenticationTokenProvider> { AndroidAuthenticationTokenProvider(get()) }
}

private class AndroidAuthenticationTokenProvider(
    private val tokenDataStore: TokenDataStore,
) : AuthenticationTokenProvider {
    override suspend fun getCurrentAccessToken() = tokenDataStore.getCurrentAccessToken()
    override suspend fun getCurrentDomain() = tokenDataStore.getCurrentDomain()
}
