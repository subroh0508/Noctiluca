package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.model.Account

interface FetchAllAuthorizedAccountsUseCase {
    suspend fun execute(): Flow<Account>
}
