package noctiluca.features.timeline.viewmodel

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.features.timeline.model.CurrentAuthorizedAccount
import noctiluca.model.account.Account
import noctiluca.model.status.Status
import noctiluca.model.timeline.StreamEvent
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.domain.usecase.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Suppress("TooManyFunctions", "LongParameterList")
class TimelinesViewModel(
    private val fetchTimelineStreamUseCase: FetchTimelineStreamUseCase,
    private val updateTimelineUseCase: UpdateTimelineUseCase,
    private val executeStatusActionUseCase: ExecuteStatusActionUseCase,
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    authorizedUserRepository: AuthorizedUserRepository,
) : AuthorizedViewModel(authorizedUserRepository), ScreenModel {
    private val subscribed by lazy { MutableStateFlow(false) }
    private val mutableUiModel by lazy { MutableStateFlow(UiModel()) }

    val uiModel: StateFlow<UiModel> by lazy {
        combine(
            authorizedAccountRepository.current(),
            authorizedAccountRepository.others(),
        ) { current, others ->
            UiModel(
                account = CurrentAuthorizedAccount(current, others),
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiModel(),
            )
    }

    fun switch(account: Account) {
        launch {
            authorizedAccountRepository.switch(account.id)
            clear()
            reopen()
        }
    }

    fun setForeground(index: Int) {
        if (uiModel.value.timelines[index].foreground) {
            set(index) { copy(scrollToTop = true) }
            return
        }

        mutableUiModel.value = uiModel.value.copy(
            timelines = uiModel.value.timelines.mapIndexed { i, state ->
                state.copy(foreground = i == index)
            }
        )
    }

    fun scrolledToTop(index: Int) {
        set(index) { copy(scrollToTop = false) }
    }

    fun subscribeAll() {
        if (subscribed.value) {
            return
        }

        subscribed.value = true
        uiModel.value.timelines.forEachIndexed { index, (timeline) -> subscribe(index, timeline) }
    }

    fun loadAll() {
        uiModel.value.timelines.forEach { (timeline) -> load(timeline) }
    }

    fun load(timeline: Timeline) {
        val index = uiModel.value.timelines.indexOfFirst { it.timeline == timeline }

        val job = launchLazy {
            runCatchingWithAuth { updateTimelineUseCase.execute(timeline) }
                .onSuccess { setTimeline(index, it) }
        }

        setJob(index, job)
        job.start()
    }

    fun favourite(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.FAVOURITE)
    fun boost(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.BOOST)
    fun bookmark(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.BOOKMARK)

    fun clear() {
        mutableUiModel.value = UiModel()
        subscribed.value = false
    }

    private fun execute(timeline: Timeline, status: Status, action: StatusAction) {
        val index = uiModel.value.timelines.indexOfFirst { it.timeline == timeline }

        val job = launchLazy {
            runCatchingWithAuth { executeStatusActionUseCase.execute(status, action) }
                .onSuccess { setStatus(index, status) }
        }

        setJob(index, job)
        job.start()
    }

    private fun subscribe(index: Int, timeline: Timeline) {
        launch {
            runCatchingWithAuth {
                fetchTimelineStreamUseCase.execute(timeline)
                    .collect { receiveEvent(index, it) }
            }
        }
    }

    private fun receiveEvent(index: Int, event: StreamEvent) {
        val current = uiModel.value.timelines[index]

        val next = when (event) {
            is StreamEvent.Updated -> current.timeline.insert(event.status)
            is StreamEvent.Deleted -> current.timeline - event.id
            is StreamEvent.StatusEdited -> current.timeline.replace(event.status)
        }

        set(index) { copy(timeline = next, latestEvent = event) }
    }

    private fun setTimeline(index: Int, timeline: Timeline) {
        set(index) { copy(timeline = timeline, jobs = jobs.filterNot { it.isCompleted }) }
    }

    private fun setStatus(index: Int, status: Status) {
        set(index) { copy(timeline = timeline.replace(status), jobs = jobs.filterNot { it.isCompleted }) }
    }

    private fun setJob(index: Int, job: Job) {
        set(index) { copy(jobs = jobs + job) }
    }

    private operator fun set(index: Int, block: TimelineState.() -> TimelineState) {
        val current = uiModel.value.timelines.toMutableList()
        current[index] = current[index].block()
        mutableUiModel.value = uiModel.value.copy(timelines = current)
    }

    data class UiModel(
        val account: CurrentAuthorizedAccount = CurrentAuthorizedAccount(),
        val timelines: List<TimelineState> = listOf(
            TimelineState(Timeline.Local(listOf(), onlyMedia = false), foreground = true),
            TimelineState(Timeline.Home(listOf())),
            TimelineState(Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false)),
        ),
    ) {
        val foreground get() = timelines.find { it.foreground }
        val currentTabIndex get() = timelines.indexOfFirst { it.foreground }
    }

    data class TimelineState(
        val timeline: Timeline,
        val jobs: List<Job> = listOf(),
        val latestEvent: StreamEvent? = null,
        val scrollToTop: Boolean = false,
        val foreground: Boolean = false,
    )

    companion object Provider {
        @Composable
        operator fun invoke(
            component: KoinComponent,
        ): TimelinesViewModel {
            return remember {
                TimelinesViewModel(
                    component.get(),
                    component.get(),
                    component.get(),
                    component.get(),
                    component.get(),
                )
            }
        }
    }
}
