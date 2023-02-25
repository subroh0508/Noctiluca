package noctiluca.test.mock

import noctiluca.repository.TokenCache
import noctiluca.test.DOMAIN_MASTODON_JP
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.me

class MockTokenCache(
    private val accessToken: String = DUMMY_ACCESS_TOKEN,
    private val domain: String = me.domain.value,
) : TokenCache {
    override suspend fun getCurrentAccessToken() = accessToken
    override suspend fun getCurrentDomain() = domain
}
