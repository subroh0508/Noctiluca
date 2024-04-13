package noctiluca.features.toot.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.di.AuthorizedContext
import noctiluca.data.status.StatusRepository
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.toot.model.TootModel
import noctiluca.model.status.Status

class TootViewModel(
    private val statusRepository: StatusRepository,
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

    fun send(
        status: String?,
        spoilerText: String?,
        visibility: Status.Visibility,
    ) {
        if (status.isNullOrBlank()) {
            return
        }

        val job = launchLazy {
            runCatchingWithAuth {
                statusRepository.new(
                    status,
                    spoilerText,
                    visibility,
                )
            }
        }

        job.start()
    }
}
