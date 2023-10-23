package noctiluca.datastore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authentication.AppCredential
import java.util.prefs.Preferences

internal class DesktopAppCredentialDataStore private constructor(
    private val prefs: Preferences,
) : AppCredentialDataStore {
    constructor() : this(Preferences.userNodeForPackage(DesktopAppCredentialDataStore::class.java))

    override suspend fun getCurrent() = withContext(Dispatchers.IO) {
        val clientId = prefs[KEY_CLIENT_ID, null] ?: return@withContext null
        val clientSecret = prefs[KEY_CLIENT_SECRET, null] ?: return@withContext null
        val domain = prefs[KEY_DOMAIN, null] ?: return@withContext null
        val authorizeUrl = prefs[KEY_AUTHORIZE_URL, null] ?: return@withContext null

        AppCredential(clientId, clientSecret, Domain(domain), Uri(authorizeUrl))
    }

    override suspend fun save(credential: AppCredential) {
        withContext(Dispatchers.IO) {
            prefs.put(KEY_CLIENT_ID, credential.clientId)
            prefs.put(KEY_CLIENT_SECRET, credential.clientSecret)
            prefs.put(KEY_DOMAIN, credential.domain.value)
            prefs.put(KEY_AUTHORIZE_URL, credential.authorizeUrl.value)
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) { prefs.clear() }
    }
}
