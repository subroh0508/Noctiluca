package app.noctiluca.shared.di

import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.network.mastodon.AuthorizationTokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun Module.AuthorizationTokenProviderModule() {
    single<AuthorizationTokenProvider> { buildTokenProvider(get()) }
}

private fun buildTokenProvider(
    dataStore: AuthorizationTokenDataStore,
) = object : AuthorizationTokenProvider {
    override suspend fun getCurrentAccessToken() = dataStore.getCurrentAccessToken()
    override suspend fun getCurrentDomain() = dataStore.getCurrentDomain()
}
