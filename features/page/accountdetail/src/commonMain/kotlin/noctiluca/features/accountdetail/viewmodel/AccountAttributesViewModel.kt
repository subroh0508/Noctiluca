package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountAttributesRepository
import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.features.accountdetail.model.AttributesModel
import noctiluca.features.shared.AuthorizeEventStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.StatusesQuery

class AccountAttributesViewModel(
    val id: AccountId,
    private val accountAttributesRepository: AccountAttributesRepository,
    eventStateFlow: AuthorizeEventStateFlow,
) : AuthorizedViewModel(eventStateFlow), ScreenModel {
    private val state by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        buildUiModel(
            accountAttributesRepository.attributes(id),
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
