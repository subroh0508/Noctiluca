package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.accountdetail.model.RelationshipsStateModel
import noctiluca.features.shared.AuthorizeEventStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.AccountId

class AccountRelationshipsViewModel(
    private val id: AccountId,
    private val accountRelationshipsRepository: AccountRelationshipsRepository,
    authorizedUserRepository: AuthorizedUserRepository,
    eventStateFlow: AuthorizeEventStateFlow,
) : AuthorizedViewModel(authorizedUserRepository, eventStateFlow), ScreenModel {
    private val state by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        buildUiModel(
            accountRelationshipsRepository.relationships(id),
            state,
            initialValue = RelationshipsStateModel(),
            catch = { e -> state.value = LoadState.Error(e) },
        ) { relationships, state ->
            RelationshipsStateModel(
                relationships = relationships,
                state = state,
            )
        }
    }

    fun follow() = launchWithAuth(state) {
        accountRelationshipsRepository.follow(id)
    }

    fun block() = launchWithAuth(state) {
        accountRelationshipsRepository.block(id)
    }

    fun mute() = launchWithAuth(state) {
        accountRelationshipsRepository.mute(id)
    }

    fun toggleReblogs() = launchWithAuth(state) {
        accountRelationshipsRepository.toggleReblog(id)
    }

    fun toggleNotify() = launchWithAuth(state) {
        accountRelationshipsRepository.toggleNotify(id)
    }
}
