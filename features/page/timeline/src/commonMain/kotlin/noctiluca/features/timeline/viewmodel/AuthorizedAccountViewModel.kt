package noctiluca.features.timeline.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.SharingStarted
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.di.AuthorizedContext
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.timeline.model.AuthorizedAccountModel
import noctiluca.model.account.Account

class AuthorizedAccountViewModel(
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    context: AuthorizedContext,
) : AuthorizedViewModel(context), ScreenModel {
    val uiModel by lazy {
        buildUiModel(
            context.state,
            authorizedAccountRepository.all(),
            initialValue = AuthorizedAccountModel(),
            started = SharingStarted.WhileSubscribed(5_000),
        ) { authorizedEventState, accounts ->
            AuthorizedAccountModel(authorizedEventState, accounts)
        }
    }

    fun switch(account: Account) {
        launch {
            runCatchingWithAuth { context.switchCurrent(account.id) }
                .onSuccess { reopen() }
        }
    }
}
