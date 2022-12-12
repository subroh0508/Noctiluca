package noctiluca.authentication.infra.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.model.Hostname

internal actual class AppCredentialCache(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val PREF_CLIENT_ID = stringPreferencesKey(KEY_CLIENT_ID)
        private val PREF_CLIENT_SECRET = stringPreferencesKey(KEY_CLIENT_SECRET)
        private val PREF_HOSTNAME = stringPreferencesKey(KEY_HOSTNAME)
    }

    actual suspend fun getCurrent(): Pair<Hostname, AppCredentialJson>? {
        val pref = dataStore.data.first()

        val clientId = pref[PREF_CLIENT_ID] ?: return null
        val clientSecret = pref[PREF_CLIENT_SECRET] ?: return null
        val hostname = pref[PREF_HOSTNAME] ?: return null

        return Hostname(hostname) to AppCredentialJson(clientId, clientSecret)
    }
    actual suspend fun save(hostname: Hostname, credential: AppCredentialJson) {
        dataStore.edit {
            it[PREF_CLIENT_ID] = credential.clientId
            it[PREF_CLIENT_SECRET] = credential.clientSecret
            it[PREF_HOSTNAME] = hostname.value
        }
    }

    actual suspend fun clear() { dataStore.edit { it.clear() } }
}
