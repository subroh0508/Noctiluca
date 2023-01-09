package noctiluca.authentication.domain.usecase

import noctiluca.model.Domain
import noctiluca.model.Uri

interface RequestAppCredentialUseCase {
    suspend fun execute(
        domain: Domain,
        clientName: String,
        redirectUri: Uri,
    ): Uri
}
