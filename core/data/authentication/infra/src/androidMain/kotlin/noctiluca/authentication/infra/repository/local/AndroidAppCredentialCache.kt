package noctiluca.authentication.infra.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import noctiluca.network.authentication.json.AppCredentialJson
import noctiluca.model.Domain

internal actual class AppCredentialCache(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val PREF_CLIENT_ID = stringPreferencesKey(KEY_CLIENT_ID)
        private val PREF_CLIENT_SECRET = stringPreferencesKey(KEY_CLIENT_SECRET)
        private val PREF_DOMAIN = stringPreferencesKey(KEY_DOMAIN)
    }

    actual suspend fun getCurrent(): Pair<Domain, AppCredentialJson>? {
        val pref = dataStore.data.first()

        val clientId = pref[PREF_CLIENT_ID] ?: return null
        val clientSecret = pref[PREF_CLIENT_SECRET] ?: return null
        val domain = pref[PREF_DOMAIN] ?: return null

        return Domain(domain) to AppCredentialJson(clientId, clientSecret)
    }
    actual suspend fun save(domain: Domain, credential: AppCredentialJson) {
        dataStore.edit {
            it[PREF_CLIENT_ID] = credential.clientId
            it[PREF_CLIENT_SECRET] = credential.clientSecret
            it[PREF_DOMAIN] = domain.value
        }
    }

    actual suspend fun clear() { dataStore.edit { it.clear() } }
}
