package noctiluca.account.infra.repository.local

import noctiluca.account.model.Account
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.Domain

interface LocalAuthorizedAccountRepository {
    suspend fun getCurrent(): Pair<Account, Domain>?
    suspend fun getAll(): List<Account>

    suspend fun getAccessToken(id: AccountId): Pair<String, Domain>?

    suspend fun save(json: AccountCredentialJson)
}
