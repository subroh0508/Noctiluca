package noctiluca.timeline.domain.usecase

import noctiluca.account.model.Account

interface FetchAllAuthorizedAccountsUseCase {
    suspend fun execute(): List<Account>
}