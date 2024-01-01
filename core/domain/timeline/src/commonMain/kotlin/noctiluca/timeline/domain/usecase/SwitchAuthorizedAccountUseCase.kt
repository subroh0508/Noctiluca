package noctiluca.timeline.domain.usecase

import noctiluca.model.AccountId

interface SwitchAuthorizedAccountUseCase {
    suspend fun execute(id: AccountId)
}
