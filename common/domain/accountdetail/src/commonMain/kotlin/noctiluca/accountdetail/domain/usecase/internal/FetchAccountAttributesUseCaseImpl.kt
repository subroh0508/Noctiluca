package noctiluca.accountdetail.domain.usecase.internal

import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.model.AccountId

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
