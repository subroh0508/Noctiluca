package app.noctiluca.di

import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.network.mastodon.AuthorizationTokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun Module.AndroidAuthorizationTokenProviderModule() {
    single<AuthorizationTokenProvider> { AndroidAuthorizationTokenProvider(get()) }
}

private class AndroidAuthorizationTokenProvider(
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
) : AuthorizationTokenProvider {
    override suspend fun getCurrentAccessToken() = authorizationTokenDataStore.getCurrentAccessToken()
    override suspend fun getCurrentDomain() = authorizationTokenDataStore.getCurrentDomain()
}
