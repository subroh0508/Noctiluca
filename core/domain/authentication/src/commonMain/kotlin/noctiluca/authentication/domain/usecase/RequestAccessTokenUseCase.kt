package noctiluca.authentication.domain.usecase

import noctiluca.model.Uri

interface RequestAccessTokenUseCase {
    suspend fun execute(
        code: String,
        redirectUri: Uri,
    ): String?
}
