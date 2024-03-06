package noctiluca.features.timeline.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.di.AuthorizedContext
import noctiluca.data.timeline.impl.TimelineStreamStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.features.timeline.model.TimelinesModel
import noctiluca.model.status.Status
import noctiluca.model.timeline.*
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.usecase.*

@Suppress("TooManyFunctions", "LongParameterList")
class TimelinesViewModel(
    private val timelineStreamStateFlow: TimelineStreamStateFlow,
    private val executeStatusActionUseCase: ExecuteStatusActionUseCase,
    private val subscribeTimelineStreamUseCase: SubscribeTimelineStreamUseCase,
    private val loadTimelineStatusesUseCase: LoadTimelineStatusesUseCase,
    context: AuthorizedContext,
) : AuthorizedViewModel(context), ScreenModel {
    private val foregroundIdStateFlow by lazy { MutableStateFlow<TimelineId>(LocalTimelineId) }
    private val loadStateFlow by lazy { MutableStateFlow<Map<TimelineId, LoadState>>(mapOf()) }

    val uiModel: StateFlow<TimelinesModel> by lazy {
        combine(
            timelineStreamStateFlow,
            foregroundIdStateFlow,
            loadStateFlow,
        ) { timelines, timelineId, loadState ->
            TimelinesModel(timelines, timelineId, loadState)
        }.onEach { model ->
            subscribe(model.tabs.isEmpty(), model.inactivates())
        }.catch { e ->
            handleException(e)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = TimelinesModel(),
        )
    }

    fun setForeground(timelineId: TimelineId) {
        foregroundIdStateFlow.value = timelineId
    }

    fun load(timelineId: TimelineId) {
        val job = launchLazy {
            runCatchingWithAuth { loadTimelineStatusesUseCase.execute(timelineId) }
                .onSuccess { loadStateFlow.value -= timelineId }
                .onFailure { loadStateFlow.value += timelineId to LoadState.Error(it) }
        }

        loadStateFlow.value += timelineId to LoadState.Loading(job)
        job.start()
    }

    fun favourite(status: Status) = execute(status, StatusAction.FAVOURITE)
    fun boost(status: Status) = execute(status, StatusAction.BOOST)
    fun bookmark(status: Status) = execute(status, StatusAction.BOOKMARK)

    private fun subscribe(
        isEmpty: Boolean,
        inactivates: Map<TimelineId, Timeline>,
    ) {
        if (!isEmpty && inactivates.isEmpty()) {
            return
        }

        val job = launchLazy {
            runCatchingWithAuth { subscribeTimelineStreamUseCase.execute(inactivates) }
                .onSuccess { loadStateFlow.value = mapOf() }
                .onFailure { e -> loadStateFlow.value += inactivates.mapValues { LoadState.Error(e) } }
        }

        loadStateFlow.value += inactivates.mapValues { LoadState.Loading(job) }
        job.start()
    }

    private fun execute(
        status: Status,
        action: StatusAction
    ) {
        launch {
            runCatchingWithAuth { executeStatusActionUseCase.execute(status, action) }
        }
    }
}
