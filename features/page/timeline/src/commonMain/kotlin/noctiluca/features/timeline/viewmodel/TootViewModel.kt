package noctiluca.features.timeline.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.di.AuthorizedContext
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.timeline.model.TootModel
import noctiluca.model.timeline.*
import noctiluca.timeline.domain.usecase.*

class TootViewModel(
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    context: AuthorizedContext,
) : AuthorizedViewModel(context), ScreenModel {
    val uiModel: StateFlow<TootModel> by lazy {
        buildUiModel(
            context.state,
            authorizedAccountRepository.all(),
            initialValue = TootModel(),
            started = SharingStarted.WhileSubscribed(5_000),
        ) { authorizedEventState, accounts ->
            TootModel(authorizedEventState, accounts)
        }
    }
}
