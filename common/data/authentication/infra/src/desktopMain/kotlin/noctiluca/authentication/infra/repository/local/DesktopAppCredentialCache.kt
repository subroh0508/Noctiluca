package noctiluca.authentication.infra.repository.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.model.Hostname
import java.util.prefs.Preferences

internal actual class AppCredentialCache(
    private val prefs: Preferences,
) {
    actual suspend fun getCurrent() = withContext(Dispatchers.IO) {
        val clientId = prefs[KEY_CLIENT_ID, null] ?: return@withContext null
        val clientSecret = prefs[KEY_CLIENT_SECRET, null] ?: return@withContext null
        val hostname = prefs[KEY_HOSTNAME, null] ?: return@withContext null

        Hostname(hostname) to AppCredentialJson(clientId, clientSecret)
    }

    actual suspend fun save(hostname: Hostname, credential: AppCredentialJson) {
        withContext(Dispatchers.IO) {
            prefs.put(KEY_CLIENT_ID, credential.clientId)
            prefs.put(KEY_CLIENT_SECRET, credential.clientSecret)
            prefs.put(KEY_HOSTNAME, hostname.value)
        }
    }

    actual suspend fun clear() { withContext(Dispatchers.IO) { prefs.clear() } }
}
