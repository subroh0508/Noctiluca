package noctiluca.authentication

import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authentication.AppCredential

interface AppCredentialRepository {
    suspend fun fetchAppCredential(
        domain: Domain,
        client: String,
        redirectUri: Uri,
    ): AppCredential
}
