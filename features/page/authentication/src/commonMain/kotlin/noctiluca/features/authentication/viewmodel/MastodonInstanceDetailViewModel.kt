package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MastodonInstanceDetailViewModel(
    val domain: String,
    private val fetchMastodonInstanceUseCase: FetchMastodonInstanceUseCase,
    private val fetchLocalTimelineUseCase: FetchLocalTimelineUseCase,
) : ViewModel(), ScreenModel {
    private val instanceLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }
    private val statuses by lazy { MutableStateFlow(listOf<Status>()) }

    val uiModel by lazy {
        combine(
            instanceLoadState,
            statuses,
        ) { instance, statuses -> UiModel(instance, statuses) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = UiModel(),
            )
    }

    fun load() {
        loadInstanceDetail()
        loadLocalTimeline()
    }

    fun loadMore() = loadLocalTimeline(uiModel.value.statuses.lastOrNull()?.id)

    private fun loadInstanceDetail() {
        val job = launchLazy {
            runCatching { fetchMastodonInstanceUseCase.execute(domain) }
                .onSuccess { instanceLoadState.value = LoadState.Loaded(it) }
                .onFailure { instanceLoadState.value = LoadState.Error(it) }
        }

        instanceLoadState.value = LoadState.Loading(job)
        job.start()
    }

    private fun loadLocalTimeline(maxId: StatusId? = null) {
        if (maxId == null && uiModel.value.statuses.isNotEmpty()) {
            return
        }

        launch {
            runCatching { fetchLocalTimelineUseCase.execute(domain, maxId) }
                .onSuccess {
                    statuses.value =
                        if (maxId == null) {
                            it
                        } else {
                            statuses.value + it
                        }
                }
                .onFailure { statuses.value = listOf() }
        }
    }

    data class UiModel(
        val instance: LoadState = LoadState.Initial,
        val statuses: List<Status> = listOf(),
    )

    companion object Provider {
        @Composable
        operator fun invoke(
            domain: String,
            koinComponent: KoinComponent,
        ): MastodonInstanceDetailViewModel {
            return remember {
                MastodonInstanceDetailViewModel(
                    domain,
                    koinComponent.get(),
                    koinComponent.get(),
                )
            }
        }
    }
}
