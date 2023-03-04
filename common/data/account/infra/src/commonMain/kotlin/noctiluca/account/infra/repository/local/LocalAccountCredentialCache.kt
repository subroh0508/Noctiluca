package noctiluca.account.infra.repository.local

import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.Domain

internal expect class LocalAccountCredentialCache {
    suspend fun get(id: AccountId, domain: Domain): AccountCredentialJson?
    suspend fun add(json: AccountCredentialJson): List<AccountCredentialJson>
    suspend fun delete(id: AccountId, domain: Domain): List<AccountCredentialJson>
}
