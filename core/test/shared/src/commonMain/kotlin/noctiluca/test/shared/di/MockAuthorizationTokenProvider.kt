package noctiluca.test.shared.di

import noctiluca.network.mastodon.AuthorizationTokenProvider
import noctiluca.test.shared.DUMMY_ACCESS_TOKEN
import noctiluca.test.shared.me
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockAuthorizationTokenProviderModule(
    accessToken: String = DUMMY_ACCESS_TOKEN,
    domain: String = me.domain.value,
) {
    single<AuthorizationTokenProvider> {
        object : AuthorizationTokenProvider {
            override suspend fun getCurrentAccessToken() = accessToken
            override suspend fun getCurrentDomain() = domain
        }
    }
}
