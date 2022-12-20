package noctiluca.authentication.infra.repository.local

import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.model.Hostname

internal const val KEY_CLIENT_ID = "CLIENT_ID"
internal const val KEY_CLIENT_SECRET = "CLIENT_SECRET"
internal const val KEY_HOSTNAME = "HOSTNAME"

internal expect class AppCredentialCache {
    suspend fun getCurrent(): Pair<Hostname, AppCredentialJson>?
    suspend fun save(hostname: Hostname, credential: AppCredentialJson)
    suspend fun clear()
}