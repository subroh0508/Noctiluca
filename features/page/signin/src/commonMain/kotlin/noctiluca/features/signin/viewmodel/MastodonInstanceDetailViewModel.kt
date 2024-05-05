package noctiluca.features.signin.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.instance.InstanceRepository
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.features.signin.component.InstancesTab
import noctiluca.features.signin.model.InstanceDetailModel

class MastodonInstanceDetailViewModel(
    private val repository: InstanceRepository,
) : ViewModel(), ScreenModel {
    private val tab by lazy { MutableStateFlow(InstancesTab.Info) }
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
        launchLazy(statusesLoadState) {
            repository.fetchMoreStatuses(domain)
        }
    }

    private fun loadInstance(domain: String) {
        launchLazy(instanceLoadState) {
            repository.fetchInstance(domain)
        }
    }

    private fun loadStatuses(domain: String) {
        launchLazy(statusesLoadState) {
            repository.fetchStatuses(domain)
        }
    }
}
