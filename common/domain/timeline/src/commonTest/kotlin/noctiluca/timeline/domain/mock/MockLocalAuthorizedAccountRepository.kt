package noctiluca.timeline.domain.mock

import noctiluca.account.infra.repository.local.LocalAuthorizedAccountRepository
import noctiluca.account.model.Account
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.Domain

class MockLocalAuthorizedAccountRepository(
    private val account: Account? = null,
    private val domain: Domain? = null,
    private val all: List<Account> = listOf(),
    private val accessToken: (AccountId) -> Pair<String, Domain>? = { null },
) : LocalAuthorizedAccountRepository {
    constructor(
        current: Pair<Account, Domain>,
        accessToken: (AccountId) -> Pair<String, Domain>? = { null },
        all: List<Account> = listOf(),
    ) : this(current.first, current.second, all, accessToken)

    private val jsonList: MutableList<AccountCredentialJson> = mutableListOf()

    val cache: List<AccountCredentialJson> get() = jsonList

    override suspend fun getCurrentAccount() = account
    override suspend fun getCurrentDomain() = domain
    override suspend fun getAll() = all

    override suspend fun getAccessToken(id: AccountId) = accessToken(id)

    override suspend fun save(json: AccountCredentialJson) {
        jsonList.add(json)
    }
}
