package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.shared.EventStateFlow
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status

class AccountDetailViewModel(
    val id: AccountId,
    private val accountDetailRepository: AccountDetailRepository,
    private val accountStatusRepository: AccountStatusRepository,
    authorizedUserRepository: AuthorizedUserRepository,
    eventStateFlow: EventStateFlow,
) : AuthorizedViewModel(authorizedUserRepository, eventStateFlow), ScreenModel {
    private val query by lazy { MutableStateFlow(StatusesQuery.DEFAULT) }

    val uiModel by lazy {
        buildUiModel(
            accountDetailRepository.attributes(id),
            accountStatusRepository.statuses(id),
            query,
            initialValue = UiModel.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        ) { account, statuses, query ->
            UiModel.Loaded(
                account = account,
                statuses = statuses,
                query = query,
            )
        }
    }

    fun switch(query: StatusesQuery) {
        this.query.value = query
    }

    fun loadStatusesMore() {
        val query = (uiModel.value as? UiModel.Loaded)?.query ?: return

        launch {
            runCatchingWithAuth { accountStatusRepository.loadStatuses(id, query) }
                .onFailure { }
        }
    }

    sealed class UiModel {
        data object Loading : UiModel()
        data class Loaded(
            val account: AccountAttributes,
            val statuses: Map<StatusesQuery, List<Status>>,
            val query: StatusesQuery,
        ) : UiModel() {

            val foreground = statuses[query] ?: listOf()
        }
    }
}
