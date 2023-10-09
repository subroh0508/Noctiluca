package noctiluca.test.di

import noctiluca.repository.TokenCache
import noctiluca.repository.TokenProvider
import noctiluca.test.mock.MockTokenCache
import noctiluca.test.mock.MockTokenProvider
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockTokenModule(
    tokenCache: MockTokenCache = MockTokenCache(),
    tokenProvider: MockTokenProvider = MockTokenProvider(),
) {
    single<TokenCache> { tokenCache }
    single<TokenProvider> { tokenProvider }
}
