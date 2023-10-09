package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.model.Uri

internal class RequestAccessTokenUseCaseImpl(
    private val repository: AuthorizedUserRepository,
) : RequestAccessTokenUseCase {
    override suspend fun execute(
        code: String,
        redirectUri: Uri,
    ) = repository.fetchAccessToken(code, redirectUri)
}
