package noctiluca.timeline.domain.usecase.internal

import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.timeline.TimelineRepository
import noctiluca.model.AccountId
import noctiluca.timeline.domain.usecase.SwitchAuthorizedAccountUseCase

internal class SwitchAuthorizedAccountUseCaseImpl(
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    private val timelineRepository: TimelineRepository,
) : SwitchAuthorizedAccountUseCase {
    override suspend fun execute(id: AccountId) {
        timelineRepository.unsubscribe()
        authorizedAccountRepository.switch(id)
    }
}
