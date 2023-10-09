package noctiluca.authentication.infra.repository.local

import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.model.Domain

internal const val KEY_CLIENT_ID = "CLIENT_ID"
internal const val KEY_CLIENT_SECRET = "CLIENT_SECRET"
internal const val KEY_DOMAIN = "DOMAIN"

internal expect class AppCredentialCache {
    suspend fun getCurrent(): Pair<Domain, AppCredentialJson>?
    suspend fun save(domain: Domain, credential: AppCredentialJson)
    suspend fun clear()
}
