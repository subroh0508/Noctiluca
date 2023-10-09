package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.model.Domain
import noctiluca.model.account.Account

interface FetchCurrentAuthorizedAccountUseCase {
    suspend fun execute(): Flow<Pair<Account, Domain>>
}
