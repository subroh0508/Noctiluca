package noctiluca.authentication.domain.mock

import noctiluca.datastore.TokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

class MockTokenDataStore(
    init: List<AuthorizedUser> = emptyList(),
) : TokenDataStore {
    private var cache = init

    override suspend fun getCurrentAccessToken() = null

    override suspend fun getCurrentDomain() = null

    override suspend fun getAccessToken(id: AccountId) = null

    override suspend fun getDomain(id: AccountId) = null

    override suspend fun getAll(): List<AuthorizedUser> = cache

    override suspend fun getCurrent(): AuthorizedUser? = cache.firstOrNull()

    override suspend fun setCurrent(id: AccountId): AuthorizedUser {
        val item = cache.find { it.id == id }

        cache = listOfNotNull(item) + cache.filterNot { it.id == id }

        return item ?: throw NoSuchElementException()
    }

    override suspend fun add(id: AccountId, domain: Domain, accessToken: String): List<AuthorizedUser> {
        val item = object : AuthorizedUser {
            override val id = id
            override val domain = domain
        }

        cache = listOf(item) + cache

        return cache
    }

    override suspend fun delete(id: AccountId): List<AuthorizedUser> {
        cache = cache.filterNot { it.id == id }

        return cache
    }
}
