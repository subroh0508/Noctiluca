package noctiluca.features.authentication.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.instance.InstanceRepository
import noctiluca.features.authentication.component.InstancesTab
import noctiluca.features.authentication.model.InstanceDetailModel
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope

class MastodonInstanceDetailViewModel(
    private val repository: InstanceRepository,
) : ViewModel(), ScreenModel {
    private val tab by lazy { MutableStateFlow(InstancesTab.INFO) }
    private val instanceLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }
    private val statusesLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }

    val uiModel by lazy {
        combine(
            repository.instance(),
            tab,
            repository.statuses(),
            instanceLoadState,
            statusesLoadState,
        ) { instance, tab, statuses, instanceLoadState, statusesLoadState ->
            InstanceDetailModel(
                instance = instance,
                tab = tab,
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
        loadInstance(domain)
        loadStatuses(domain)
    }

    fun switch(tab: InstancesTab) {
        this.tab.value = tab
    }

    fun loadMoreStatuses(domain: String) {
        val job = launchLazy {
            runCatching { repository.fetchMoreStatuses(domain) }
                .onSuccess { statusesLoadState.value = LoadState.Loaded(Unit) }
                .onFailure { statusesLoadState.value = LoadState.Error(it) }
        }

        statusesLoadState.value = LoadState.Loading(job)
        job.start()
    }

    private fun loadInstance(domain: String) {
        val job = launchLazy {
            runCatching { repository.fetchInstance(domain) }
                .onSuccess { instanceLoadState.value = LoadState.Loaded(Unit) }
                .onFailure { instanceLoadState.value = LoadState.Error(it) }
        }

        instanceLoadState.value = LoadState.Loading(job)
        job.start()
    }

    private fun loadStatuses(domain: String) {
        val job = launchLazy {
            runCatching { repository.fetchStatuses(domain) }
                .onSuccess { statusesLoadState.value = LoadState.Loaded(Unit) }
                .onFailure { statusesLoadState.value = LoadState.Error(it) }
        }

        statusesLoadState.value = LoadState.Loading(job)
        job.start()
    }
}
