package noctiluca.features.timeline.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.timeline.impl.TimelineStreamStateFlow
import noctiluca.features.shared.AuthorizeEventStateFlow
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.timeline.model.CurrentAuthorizedAccount
import noctiluca.model.account.Account
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
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    authorizedUserRepository: AuthorizedUserRepository,
    eventStateFlow: AuthorizeEventStateFlow,
) : AuthorizedViewModel(authorizedUserRepository, eventStateFlow), ScreenModel {
    private val foregroundIdStateFlow by lazy { MutableStateFlow<TimelineId>(LocalTimelineId) }
    private val loadStateFlow by lazy { MutableStateFlow<Map<TimelineId, LoadState>>(mapOf()) }

    val uiModel: StateFlow<UiModel> by lazy {
        buildUiModel(
            authorizedAccountRepository.current(),
            authorizedAccountRepository.others(),
            timelineStreamStateFlow,
            foregroundIdStateFlow,
            loadStateFlow,
            initialValue = UiModel(),
            started = SharingStarted.Eagerly,
        ) { current, others, timelines, timelineId, loadState ->
            UiModel(
                account = CurrentAuthorizedAccount(current, others),
                timelines = timelines.map { id, timeline, latestEvent ->
                    id to TimelineState(
                        timeline = timeline,
                        latestEvent = latestEvent,
                        foreground = timelineId == id,
                    )
                },
                loadState = loadState,
            )
        }
    }

    fun switch(account: Account) {
        launch {
            runCatchingWithAuth { authorizedAccountRepository.switch(account.id) }
                .onSuccess { reopen() }
        }
    }

    fun setForeground(timelineId: TimelineId) {
        foregroundIdStateFlow.value = timelineId
    }

    fun subscribe() {
        val job = launchLazy {
            runCatchingWithAuth { subscribeTimelineStreamUseCase.execute() }
                .onSuccess { loadStateFlow.value = mapOf() }
                .onFailure { e -> loadStateFlow.value += uiModel.value.timelines.keys.associateWith { LoadState.Error(e) } }
        }

        loadStateFlow.value += uiModel.value.timelines.keys.associateWith { LoadState.Loading(job) }
        job.start()
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

    private fun execute(
        status: Status,
        action: StatusAction
    ) {
        launch {
            runCatchingWithAuth { executeStatusActionUseCase.execute(status, action) }
        }
    }

    data class UiModel(
        val account: CurrentAuthorizedAccount = CurrentAuthorizedAccount(),
        val timelines: Map<TimelineId, TimelineState> = mapOf(),
        val loadState: Map<TimelineId, LoadState> = mapOf(),
    ) {
        val foreground get() = timelines.values.find { it.foreground }
        val currentTabIndex get() = timelines.values.indexOfFirst { it.foreground }

        fun toTimelineList() = timelines.values.toList()
        fun findTimelineId(index: Int) = timelines.keys.toList()[index]
    }

    data class TimelineState(
        val timeline: Timeline,
        val latestEvent: StreamEvent? = null,
        val foreground: Boolean = false,
    )
}
