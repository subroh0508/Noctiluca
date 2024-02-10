package noctiluca.features.authentication.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.instance.InstanceRepository
import noctiluca.features.authentication.model.InstanceSuggestsModel
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.viewModelScope

class MastodonInstanceListViewModel(
    private val repository: InstanceRepository,
) : ViewModel(), ScreenModel {
    private val state by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        combine(
            repository.suggests(),
            state,
        ) { suggests, state ->
            InstanceSuggestsModel(
                suggests = if (state.loading) listOf() else suggests,
                state = state,
            )
        }.catch {
            state.value = LoadState.Error(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = InstanceSuggestsModel(),
        )
    }

    fun search(query: String) {
        val job = launch {
            runCatching { repository.search(query) }
                .onSuccess { state.value = LoadState.Loaded(Unit) }
                .onFailure { state.value = LoadState.Error(it) }
        }

        state.value = LoadState.Loading(job)
    }
}
