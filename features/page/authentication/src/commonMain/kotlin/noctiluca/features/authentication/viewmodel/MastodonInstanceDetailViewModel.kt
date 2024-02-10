package noctiluca.features.authentication.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.instance.InstanceRepository
import noctiluca.features.authentication.model.InstanceDetailModel
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.viewModelScope

class MastodonInstanceDetailViewModel(
    val domain: String,
    private val repository: InstanceRepository,
) : ViewModel(), ScreenModel {
    private val instanceLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }
    private val statusesLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        combine(
            repository.instance(domain)
                .catch { instanceLoadState.value = LoadState.Error(it) }
                .onCompletion { instanceLoadState.value = LoadState.Loaded(Unit) },
            repository.statuses(domain)
                .catch { statusesLoadState.value = LoadState.Error(it) },
            instanceLoadState,
            statusesLoadState,
        ) { instance, statuses, instanceLoadState, statusesLoadState ->
            InstanceDetailModel(
                instance = instance,
                statuses = statuses,
                instanceLoadState = instanceLoadState,
                statusesLoadState = statusesLoadState,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = InstanceDetailModel(),
        )
    }

    fun loadMore() {
        launch {
            runCatching { repository.loadStatuses(domain) }
                .onSuccess { statusesLoadState.value = LoadState.Loaded(Unit) }
                .onFailure { statusesLoadState.value = LoadState.Error(it) }
        }
    }
}
