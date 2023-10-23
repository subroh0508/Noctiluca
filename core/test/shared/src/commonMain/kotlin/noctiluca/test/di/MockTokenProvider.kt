package noctiluca.test.di

import noctiluca.network.mastodon.TokenProvider
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.me
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockTokenProviderModule(
    accessToken: String = DUMMY_ACCESS_TOKEN,
    domain: String = me.domain.value,
) {
    single<TokenProvider> {
        object : TokenProvider {
            override suspend fun getCurrentAccessToken() = accessToken
            override suspend fun getCurrentDomain() = domain
        }
    }
}
