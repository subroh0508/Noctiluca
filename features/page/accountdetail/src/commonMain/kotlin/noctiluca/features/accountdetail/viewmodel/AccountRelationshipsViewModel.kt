package noctiluca.features.accountdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.shared.AuthorizeEventStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.Relationships

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
            initialValue = UiModel(),
            catch = { e -> state.value = LoadState.Error(e) },
        ) { relationships, state ->
            UiModel(
                relationships = relationships,
                state = state,
            )
        }
    }

    fun follow() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.follow(id) }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

    fun block() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.block(id) }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

    fun mute() {
        val job = launch {
            runCatchingWithAuth { accountRelationshipsRepository.mute(id) }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

    data class UiModel(
        val relationships: Relationships = Relationships.NONE,
        val state: LoadState = LoadState.Initial,
    )
}
