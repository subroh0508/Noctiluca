package noctiluca.test.mock

import noctiluca.repository.TokenCache
import noctiluca.test.DOMAIN_MASTODON_JP
import noctiluca.test.DUMMY_ACCESS_TOKEN

class MockTokenCache : TokenCache {
    override suspend fun getCurrentAccessToken() = DUMMY_ACCESS_TOKEN
    override suspend fun getCurrentDomain() = DOMAIN_MASTODON_JP
}