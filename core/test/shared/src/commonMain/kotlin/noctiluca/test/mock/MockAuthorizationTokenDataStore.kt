package noctiluca.test.mock

import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.me

fun buildEmptyMockAuthenticationTokenDataStore() = MockAuthorizationTokenDataStore(
    init = emptyList(),
    currentAccessToken = null,
)

fun buildFilledMockAuthenticationTokenDataStore(
    init: List<AuthorizedUser> = listOf(me),
    currentAccessToken: String? = DUMMY_ACCESS_TOKEN,
) = MockAuthorizationTokenDataStore(
    init = init,
    currentAccessToken = currentAccessToken,
)

class MockAuthorizationTokenDataStore internal constructor(
    init: List<AuthorizedUser>,
    private val currentAccessToken: String?,
    private val getCache: (AccountId) -> Pair<String, Domain>? = { null },
) : AuthorizationTokenDataStore {
    constructor(
        init: List<AuthorizedUser>,
        getCache: (AccountId) -> Pair<String, Domain>?
    ) : this(init, null, getCache)

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

        cache += listOf(item)

        return cache
    }

    override suspend fun delete(id: AccountId): List<AuthorizedUser> {
        cache = cache.filterNot { it.id == id }

        return cache
    }
}
