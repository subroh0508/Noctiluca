package noctiluca.account.infra.repository.local

import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId

interface LocalAccountCredentialCache {
    suspend fun get(id: AccountId): AccountCredentialJson?
    suspend fun add(json: AccountCredentialJson): List<AccountCredentialJson>
    suspend fun delete(id: AccountId): List<AccountCredentialJson>
}
