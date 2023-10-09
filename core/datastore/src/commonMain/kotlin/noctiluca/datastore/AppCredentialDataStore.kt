package noctiluca.datastore

import noctiluca.model.AppCredential

internal const val KEY_CLIENT_ID = "CLIENT_ID"
internal const val KEY_CLIENT_SECRET = "CLIENT_SECRET"
internal const val KEY_DOMAIN = "DOMAIN"
internal const val KEY_AUTHORIZE_URL = "AUTHORIZE_URL"

internal expect class AppCredentialDataStore {
    suspend fun getCurrent(): AppCredential?
    suspend fun save(credential: AppCredential)
    suspend fun clear()
}
