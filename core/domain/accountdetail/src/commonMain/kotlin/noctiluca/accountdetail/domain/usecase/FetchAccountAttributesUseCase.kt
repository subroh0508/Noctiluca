package noctiluca.accountdetail.domain.usecase

import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes

interface FetchAccountAttributesUseCase {
    suspend fun execute(id: AccountId): AccountAttributes
}
