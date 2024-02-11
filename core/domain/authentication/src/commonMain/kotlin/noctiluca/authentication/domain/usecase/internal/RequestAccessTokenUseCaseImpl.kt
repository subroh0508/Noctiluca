package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri

internal class RequestAccessTokenUseCaseImpl(
    private val repository: AuthorizedUserRepository,
) : RequestAccessTokenUseCase {
    override suspend fun execute(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser? {
        /*
        val user = repository.fetch(code, redirectUri) ?: return null

        return repository.switch(user.id)
        */
        return null
    }
}
