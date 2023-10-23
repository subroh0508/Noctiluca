package noctiluca.test.di

import noctiluca.network.mastodon.TokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockTokenProviderModule() {
    single<TokenProvider> {
        object : TokenProvider {
            override suspend fun getCurrentAccessToken() = null
            override suspend fun getCurrentDomain() = null
        }
    }
}
