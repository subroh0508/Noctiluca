package noctiluca.accountdetail.domain.usecase.internal

import noctiluca.accountdetail.domain.usecase.FetchAccountDetailUseCase
import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.accountdetail.model.AccountDetail
import noctiluca.model.AccountId

internal class FetchAccountDetailUseCaseImpl(
    private val repository: AccountDetailRepository,
) : FetchAccountDetailUseCase {
    override suspend fun execute(id: AccountId): AccountDetail {
        val account = repository.fetch(id)

        return runCatching { repository.fetchRelationships(id) }
            .getOrNull()
            ?.let { account.copy(relationships = it) }
            ?: account
    }
}
