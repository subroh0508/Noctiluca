package app.noctiluca.di

import noctiluca.datastore.TokenDataStore
import noctiluca.network.mastodon.TokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun Module.AndroidTokenProviderModule() {
    single<TokenProvider> { AndroidTokenProvider(get()) }
}

private class AndroidTokenProvider(
    private val tokenDataStore: TokenDataStore,
) : TokenProvider {
    override suspend fun getCurrentAccessToken() = tokenDataStore.getCurrentAccessToken()
    override suspend fun getCurrentDomain() = tokenDataStore.getCurrentDomain()
}
