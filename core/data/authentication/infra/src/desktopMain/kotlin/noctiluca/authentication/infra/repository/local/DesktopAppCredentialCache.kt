package noctiluca.authentication.infra.repository.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noctiluca.network.authentication.json.AppCredentialJson
import noctiluca.model.Domain
import java.util.prefs.Preferences

internal actual class AppCredentialCache(
    private val prefs: Preferences,
) {
    actual suspend fun getCurrent() = withContext(Dispatchers.IO) {
        val clientId = prefs[KEY_CLIENT_ID, null] ?: return@withContext null
        val clientSecret = prefs[KEY_CLIENT_SECRET, null] ?: return@withContext null
        val domain = prefs[KEY_DOMAIN, null] ?: return@withContext null

        Domain(domain) to AppCredentialJson(clientId, clientSecret)
    }

    actual suspend fun save(domain: Domain, credential: AppCredentialJson) {
        withContext(Dispatchers.IO) {
            prefs.put(KEY_CLIENT_ID, credential.clientId)
            prefs.put(KEY_CLIENT_SECRET, credential.clientSecret)
            prefs.put(KEY_DOMAIN, domain.value)
        }
    }

    actual suspend fun clear() { withContext(Dispatchers.IO) { prefs.clear() } }
}
