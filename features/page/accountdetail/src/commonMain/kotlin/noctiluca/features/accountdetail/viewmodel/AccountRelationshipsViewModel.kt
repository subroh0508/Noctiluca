package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.accountdetail.model.RelationshipsStateModel
import noctiluca.features.shared.AuthorizeEventStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
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

    fun follow() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.follow(id) }
                .onSuccess { state.value = LoadState.Initial }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

    fun block() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.block(id) }
                .onSuccess { state.value = LoadState.Initial }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

    fun mute() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.mute(id) }
                .onSuccess { state.value = LoadState.Initial }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

    fun toggleReblogs() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.toggleReblog(id) }
                .onSuccess { state.value = LoadState.Initial }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

    fun toggleNotify() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.toggleNotify(id) }
                .onSuccess { state.value = LoadState.Initial }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }
}
