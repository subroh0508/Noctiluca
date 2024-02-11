package noctiluca.data.mock

import noctiluca.datastore.AppCredentialDataStore
import noctiluca.model.authentication.AppCredential

class MockAppCredentialDataStore(
    init: AppCredential? = null,
) : AppCredentialDataStore {
    private var appCredential = init

    override suspend fun getCurrent(): AppCredential? = appCredential
    override suspend fun save(credential: AppCredential) {
        appCredential = credential
    }

    override suspend fun clear() {
        appCredential = null
    }
}
