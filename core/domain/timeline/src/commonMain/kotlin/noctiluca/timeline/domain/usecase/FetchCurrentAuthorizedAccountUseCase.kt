package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.timeline.domain.model.CurrentAuthorizedAccount

interface FetchCurrentAuthorizedAccountUseCase {
    suspend fun execute(): Flow<CurrentAuthorizedAccount>
}
