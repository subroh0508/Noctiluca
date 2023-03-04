package noctiluca.api.token

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import noctiluca.api.token.internal.Token
import noctiluca.api.token.internal.find
import noctiluca.api.token.internal.hasSameIdentifier
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.repository.TokenCache
import java.util.prefs.Preferences

@Suppress("UNCHECKED_CAST")
actual class LocalTokenCache internal constructor(
    private val prefs: Preferences,
) : TokenCache {
    companion object {
        private const val TOKEN_LIST = "TOKEN_LIST"
    }

    override suspend fun getCurrentAccessToken() = (getCurrent() as? Token)?.accessToken

    override suspend fun getCurrentDomain() = (getCurrent() as? Token)?.domain?.value

    actual suspend fun getAll(): List<AuthorizedUser> = withContext(Dispatchers.IO) {
        val current = getCurrent()

        listOfNotNull(current) + getTokensJson().filterNot {
            current != null && it.hasSameIdentifier(current.id, current.domain)
        }.map(::Token)
    }

    actual suspend fun getCurrent(): AuthorizedUser? = withContext(Dispatchers.IO) {
        getTokensJson().find(Token.Json::current)?.let(::Token)
    }

    actual suspend fun setCurrent(id: AccountId, domain: Domain): AuthorizedUser =
        withContext(Dispatchers.IO) {
            val prevTokens = getTokensJson()

            val token = prevTokens.find(id, domain)
            val nextTokens = listOfNotNull(token?.copy(current = true)) + prevTokens.mapNotNull {
                if (it.hasSameIdentifier(id, domain)) {
                    return@mapNotNull null
                }

                it.copy(current = false)
            }

            saveTokensJson(nextTokens)

            Token(getTokensJson().first(Token.Json::current))
        }

    actual suspend fun getAccessToken(id: AccountId, domain: Domain) = withContext(Dispatchers.IO) {
        getTokensJson().find(id, domain)?.accessToken
    }

    actual suspend fun add(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ) = withContext(Dispatchers.IO) {
        val nextTokens = getTokensJson() + listOf(Token.Json(id, domain, accessToken))
        saveTokensJson(nextTokens)

        nextTokens as List<AuthorizedUser>
    }

    actual suspend fun delete(id: AccountId, domain: Domain) = withContext(Dispatchers.IO) {
        val nextTokens = getTokensJson().filterNot { it.hasSameIdentifier(id, domain) }
        saveTokensJson(nextTokens)

        nextTokens as List<AuthorizedUser>
    }

    private fun getTokensJson() = prefs[TOKEN_LIST, null]?.let {
        Json.decodeFromString<Array<Token.Json>>(it)
    }?.toList() ?: listOf()

    private fun saveTokensJson(tokens: List<Token.Json>) {
        prefs.put(TOKEN_LIST, Json.encodeToString(tokens.toTypedArray()))
    }
}
