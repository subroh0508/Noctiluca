package noctiluca.authentication.domain.usecase

import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri

interface RequestAccessTokenUseCase {
    suspend fun execute(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser?
}
