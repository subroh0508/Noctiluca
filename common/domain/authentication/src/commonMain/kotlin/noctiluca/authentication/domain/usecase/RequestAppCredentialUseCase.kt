package noctiluca.authentication.domain.usecase

import noctiluca.model.Hostname
import noctiluca.model.Uri

interface RequestAppCredentialUseCase {
    suspend fun execute(
        hostname: Hostname,
        clientName: String,
        redirectUri: Uri,
    ): Uri
}
