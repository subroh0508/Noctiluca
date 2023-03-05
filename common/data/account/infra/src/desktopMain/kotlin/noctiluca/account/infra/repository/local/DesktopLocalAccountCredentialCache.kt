package noctiluca.account.infra.repository.local

import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import java.util.prefs.Preferences

internal class DesktopLocalAccountCredentialCache(
    private val json: Json,
    private val prefs: Preferences,
) : LocalAccountCredentialCache {
    override suspend fun get(id: AccountId): AccountCredentialJson? = null
    override suspend fun add(json: AccountCredentialJson): List<AccountCredentialJson> = listOf()
    override suspend fun delete(id: AccountId): List<AccountCredentialJson> = listOf()
}
