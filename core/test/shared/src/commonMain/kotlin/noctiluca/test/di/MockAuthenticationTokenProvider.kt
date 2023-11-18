package noctiluca.test.di

import noctiluca.network.mastodon.AuthenticationTokenProvider
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.me
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockAuthenticationTokenProviderModule(
    accessToken: String = DUMMY_ACCESS_TOKEN,
    domain: String = me.domain.value,
) {
    single<AuthenticationTokenProvider> {
        object : AuthenticationTokenProvider {
            override suspend fun getCurrentAccessToken() = accessToken
            override suspend fun getCurrentDomain() = domain
        }
    }
}
