package noctiluca.accountdetail.domain.usecase

import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.model.AccountId

interface FetchAccountAttributesUseCase {
    suspend fun execute(id: AccountId): AccountAttributes
}
