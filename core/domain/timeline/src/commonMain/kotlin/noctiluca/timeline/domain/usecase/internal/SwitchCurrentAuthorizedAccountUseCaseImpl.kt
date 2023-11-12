package noctiluca.timeline.domain.usecase.internal

import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.model.AccountId
import noctiluca.timeline.domain.usecase.SwitchCurrentAuthorizedAccountUseCase

internal class SwitchCurrentAuthorizedAccountUseCaseImpl(
    private val repository: AuthorizedUserRepository,
) : SwitchCurrentAuthorizedAccountUseCase {
    override suspend fun execute(
        id: AccountId
    ) = repository.switch(id)
}
