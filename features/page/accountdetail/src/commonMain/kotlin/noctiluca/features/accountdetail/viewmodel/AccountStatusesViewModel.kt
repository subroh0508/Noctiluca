package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.accountdetail.model.StatusesTabModel
import noctiluca.features.shared.AuthorizeEventStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.StatusesQuery

class AccountStatusesViewModel(
    val id: AccountId,
    private val accountStatusRepository: AccountStatusRepository,
    authorizedUserRepository: AuthorizedUserRepository,
    eventStateFlow: AuthorizeEventStateFlow,
) : AuthorizedViewModel(authorizedUserRepository, eventStateFlow), ScreenModel {
    private val query by lazy { MutableStateFlow(StatusesQuery.DEFAULT) }
    private val state by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        buildUiModel(
            query,
            accountStatusRepository.statuses(id),
            state,
            initialValue = StatusesTabModel(),
            catch = { e -> state.value = LoadState.Error(e) },
        ) { query, statuses, state ->
            StatusesTabModel(
                query = query,
                statuses = statuses,
                state = state,
            )
        }
    }

    fun switch(query: StatusesQuery) {
        this.query.value = query
    }

    fun loadStatusesMore() {
        if (uiModel.value.state.loading) {
            return
        }
        val query = uiModel.value.query

        launchWithAuth(state) {
            accountStatusRepository.loadStatuses(id, query)
        }
    }
}
