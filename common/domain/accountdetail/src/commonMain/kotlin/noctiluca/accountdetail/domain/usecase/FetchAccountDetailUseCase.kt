package noctiluca.accountdetail.domain.usecase

import noctiluca.accountdetail.model.AccountDetail
import noctiluca.model.AccountId

interface FetchAccountDetailUseCase {
    suspend fun execute(id: AccountId): AccountDetail
}
