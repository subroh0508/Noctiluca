package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.model.Account
import noctiluca.model.Domain

interface FetchCurrentAuthorizedAccountUseCase {
    suspend fun execute(): Flow<Pair<Account, Domain>>
}
