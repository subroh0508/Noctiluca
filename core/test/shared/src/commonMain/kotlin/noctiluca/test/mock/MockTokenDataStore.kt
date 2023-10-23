package noctiluca.test.mock

import noctiluca.datastore.TokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.account.Account
import noctiluca.test.model.MockAuthorizedUser

class MockTokenDataStore(
    init: List<AuthorizedUser> = emptyList(),
    private val currentAccessToken: String? = null,
    private val getCache: (AccountId) -> Pair<String, Domain>? = { null },
) : TokenDataStore {
    constructor(
        id: AccountId,
        domain: Domain,
    ) : this(listOf(MockAuthorizedUser(id, domain)))

    constructor(
        vararg init: Pair<Account, Domain>,
    ) : this(
        init.map { (account, domain) -> MockAuthorizedUser(account.id, domain) },
    )

    private var cache = init

    override suspend fun getCurrentAccessToken() = currentAccessToken

    override suspend fun getCurrentDomain() = getCurrent()?.domain?.value

    override suspend fun getAccessToken(id: AccountId) = getCache(id)?.first

    override suspend fun getDomain(id: AccountId) = getCache(id)?.second

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
