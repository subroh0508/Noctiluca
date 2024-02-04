package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.accountdetail.model.AttributesModel
import noctiluca.features.shared.AuthorizeEventStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.StatusesQuery

class AccountDetailViewModel(
    val id: AccountId,
    private val accountDetailRepository: AccountDetailRepository,
    authorizedUserRepository: AuthorizedUserRepository,
    eventStateFlow: AuthorizeEventStateFlow,
) : AuthorizedViewModel(authorizedUserRepository, eventStateFlow), ScreenModel {
    private val state by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        buildUiModel(
            accountDetailRepository.attributes(id),
            state,
            initialValue = AttributesModel(),
            catch = { e -> state.value = LoadState.Error(e) },
        ) { attributes, state ->
            AttributesModel(
                attributes = attributes,
                state = state,
            )
        }
    }

    fun switch(query: StatusesQuery) = Unit

    fun loadStatusesMore() = Unit
}
