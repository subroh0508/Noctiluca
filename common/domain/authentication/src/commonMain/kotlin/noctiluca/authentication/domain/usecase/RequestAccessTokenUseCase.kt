package noctiluca.authentication.domain.usecase

import noctiluca.model.AuthorizedUser
import noctiluca.model.Hostname
import noctiluca.model.Uri

interface RequestAccessTokenUseCase {
    suspend fun execute(
        hostname: Hostname,
        redirectUri: Uri,
        code: String,
    ): AuthorizedUser?
}