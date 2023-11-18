package app.noctiluca.di

import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.network.mastodon.AuthenticationTokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun Module.AndroidAuthenticationTokenProviderModule() {
    single<AuthenticationTokenProvider> { AndroidAuthenticationTokenProvider(get()) }
}

private class AndroidAuthenticationTokenProvider(
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
) : AuthenticationTokenProvider {
    override suspend fun getCurrentAccessToken() = authenticationTokenDataStore.getCurrentAccessToken()
    override suspend fun getCurrentDomain() = authenticationTokenDataStore.getCurrentDomain()
}
