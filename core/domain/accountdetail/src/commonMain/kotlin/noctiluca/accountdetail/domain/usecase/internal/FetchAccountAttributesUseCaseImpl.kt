package noctiluca.accountdetail.domain.usecase.internal

import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes

internal class FetchAccountAttributesUseCaseImpl(
    private val repository: AccountDetailRepository,
) : FetchAccountAttributesUseCase {
    override suspend fun execute(id: AccountId): AccountAttributes {
        val account = repository.fetch(id)

        return runCatching { repository.fetchRelationships(id) }
            .getOrNull()
            ?.let { account.copy(relationships = it) }
            ?: account
    }
}
