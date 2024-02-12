package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.features.shared.context.AuthorizedContext
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.AccountId

class AccountRelationshipsViewModel(
    private val id: AccountId,
    private val accountRelationshipsRepository: AccountRelationshipsRepository,
    context: AuthorizedContext,
) : AuthorizedViewModel(context), ScreenModel {
    private val state by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        buildUiModel(
            accountRelationshipsRepository.relationships(id),
            state,
            initialValue = RelationshipsModel(),
            catch = { e -> state.value = LoadState.Error(e) },
        ) { relationships, state ->
            RelationshipsModel(
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
