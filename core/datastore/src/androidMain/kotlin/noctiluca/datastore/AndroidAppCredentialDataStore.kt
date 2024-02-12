package noctiluca.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authorization.AppCredential

internal class AndroidAppCredentialDataStore(
    private val dataStore: DataStore<Preferences>,
) : AppCredentialDataStore {
    companion object {
        private val PREF_CLIENT_ID = stringPreferencesKey(KEY_CLIENT_ID)
        private val PREF_CLIENT_SECRET = stringPreferencesKey(KEY_CLIENT_SECRET)
        private val PREF_DOMAIN = stringPreferencesKey(KEY_DOMAIN)
        private val PREF_AUTHORIZE_URL = stringPreferencesKey(KEY_AUTHORIZE_URL)
    }

    override suspend fun getCurrent(): AppCredential? {
        val pref = dataStore.data.first()

        val clientId = pref[PREF_CLIENT_ID] ?: return null
        val clientSecret = pref[PREF_CLIENT_SECRET] ?: return null
        val domain = pref[PREF_DOMAIN] ?: return null
        val authorizeUrl = pref[PREF_AUTHORIZE_URL] ?: return null

        return AppCredential(clientId, clientSecret, Domain(domain), Uri(authorizeUrl))
    }

    override suspend fun save(credential: AppCredential) {
        dataStore.edit {
            it[PREF_CLIENT_ID] = credential.clientId
            it[PREF_CLIENT_SECRET] = credential.clientSecret
            it[PREF_DOMAIN] = credential.domain.value
            it[PREF_AUTHORIZE_URL] = credential.authorizeUrl.value
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
