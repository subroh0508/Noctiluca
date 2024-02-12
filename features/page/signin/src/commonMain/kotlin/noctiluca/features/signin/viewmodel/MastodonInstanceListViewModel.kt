package noctiluca.features.signin.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.instance.InstanceRepository
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.features.signin.model.InstanceSuggestsModel

class MastodonInstanceListViewModel(
    private val repository: InstanceRepository,
) : ViewModel(), ScreenModel {
    private val query by lazy { MutableStateFlow("") }
    private val state by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        combine(
            repository.suggests(),
            query,
            state,
        ) { suggests, query, state ->
            InstanceSuggestsModel(
                suggests = if (state.loading) listOf() else suggests,
                state = state,
                query = query,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = InstanceSuggestsModel(),
        )
    }

    fun search(query: String) {
        val prevQuery = uiModel.value.query
        if (query == prevQuery) {
            return
        }

        this.query.value = query

        val job = launchLazy {
            runCatching { repository.search(query) }
                .onSuccess {
                    state.value =
                        if (query.isBlank()) {
                            LoadState.Initial
                        } else {
                            LoadState.Loaded
                        }
                }
                .onFailure { state.value = LoadState.Error(it) }
        }

        state.value = LoadState.Loading(job)
        job.start()
    }
}
