package noctiluca.features.timeline.viewmodel

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.timeline.TimelineRepository
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.features.timeline.model.CurrentAuthorizedAccount
import noctiluca.model.account.Account
import noctiluca.model.status.Status
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.usecase.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Suppress("TooManyFunctions", "LongParameterList")
class TimelinesViewModel(
    private val executeStatusActionUseCase: ExecuteStatusActionUseCase,
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    private val timelineRepository: TimelineRepository,
    authorizedUserRepository: AuthorizedUserRepository,
) : AuthorizedViewModel(authorizedUserRepository), ScreenModel {
    private val mutableUiModel by lazy { MutableStateFlow(UiModel()) }
    private val foregroundIndexStateFlow by lazy { MutableStateFlow(0) }

    val uiModel: StateFlow<UiModel> by lazy {
        combine(
            authorizedAccountRepository.current(),
            authorizedAccountRepository.others(),
            timelineRepository.buildStream(),
            foregroundIndexStateFlow,
        ) { current, others, timelines, index ->
            UiModel(
                account = CurrentAuthorizedAccount(current, others),
                timelines = timelines.mapIndexed { i, timeline ->
                    TimelineState(
                        timeline = timeline,
                        foreground = i == index,
                    )
                },
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = UiModel(),
            )
    }

    fun switch(account: Account) {
        launch {
            authorizedAccountRepository.switch(account.id)
            timelineRepository.close()
            reopen()
        }
    }

    fun setForeground(index: Int) {
        foregroundIndexStateFlow.value = index
    }

    fun scrolledToTop(index: Int) {
        set(index) { copy(scrollToTop = false) }
    }

    fun subscribeAll() {
        launch {
            runCatchingWithAuth { timelineRepository.start() }
        }
    }

    fun load(timeline: Timeline) {
        launch {
            runCatchingWithAuth { timelineRepository.load(timeline) }
        }
    }

    fun favourite(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.FAVOURITE)
    fun boost(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.BOOST)
    fun bookmark(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.BOOKMARK)

    fun clear() {
        launch {
            timelineRepository.close()
        }
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
                )
            }
        }
    }
}
