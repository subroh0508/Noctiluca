package noctiluca.features.authentication.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.instance.InstanceRepository
import noctiluca.features.authentication.model.InstanceDetailModel
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope

class MastodonInstanceDetailViewModel(
    private val repository: InstanceRepository,
) : ViewModel(), ScreenModel {
    private val instanceLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }
    private val statusesLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        combine(
            repository.instance(),
            repository.statuses(),
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

    fun load(domain: String) {
        val job = launchLazy {
            runCatching { repository.fetchInstance(domain) }
                .onSuccess { instanceLoadState.value = LoadState.Loaded(Unit) }
                .onFailure { instanceLoadState.value = LoadState.Error(it) }
        }

        instanceLoadState.value = LoadState.Loading(job)
        job.start()
    }

    fun loadMore(domain: String) {
        val job = launchLazy {
            runCatching { repository.loadStatuses(domain) }
                .onSuccess { statusesLoadState.value = LoadState.Loaded(Unit) }
                .onFailure { statusesLoadState.value = LoadState.Error(it) }
        }

        statusesLoadState.value = LoadState.Loading(job)
        job.start()
    }
}
