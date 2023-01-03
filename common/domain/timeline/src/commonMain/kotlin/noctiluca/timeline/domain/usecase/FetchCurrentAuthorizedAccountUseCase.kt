package noctiluca.timeline.domain.usecase

import noctiluca.account.model.Account

interface FetchCurrentAuthorizedAccountUseCase {
    suspend fun execute(): Account
}
