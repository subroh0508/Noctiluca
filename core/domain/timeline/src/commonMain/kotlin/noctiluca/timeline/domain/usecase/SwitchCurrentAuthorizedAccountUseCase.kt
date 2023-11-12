package noctiluca.timeline.domain.usecase

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser

interface SwitchCurrentAuthorizedAccountUseCase {
    suspend fun execute(id: AccountId): AuthorizedUser
}
