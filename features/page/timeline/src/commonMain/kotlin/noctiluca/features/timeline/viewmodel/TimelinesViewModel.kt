package noctiluca.features.timeline.viewmodel

import androidx.compose.runtime.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.Domain
import noctiluca.model.account.Account
import noctiluca.model.status.Status
import noctiluca.model.timeline.StreamEvent
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.domain.usecase.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Suppress("TooManyFunctions", "LongParameterList")
class TimelinesViewModel private constructor(
    private val fetchCurrentAuthorizedAccountUseCase: FetchCurrentAuthorizedAccountUseCase,
    private val fetchAllAuthorizedAccountsUseCase: FetchAllAuthorizedAccountsUseCase,
    private val fetchTimelineStreamUseCase: FetchTimelineStreamUseCase,
    private val updateTimelineUseCase: UpdateTimelineUseCase,
    private val executeStatusActionUseCase: ExecuteStatusActionUseCase,
    authorizedUserRepository: AuthorizedUserRepository,
    coroutineScope: CoroutineScope,
) : AuthorizedViewModel(authorizedUserRepository, coroutineScope) {
    private val mutableUiModel by lazy { MutableValue(UiModel()) }

    val uiModel: Value<UiModel> = mutableUiModel

    fun switch(account: Account) {
        launch {
            authorizedUserRepository.switch(account.id)
            reopen()
        }
    }

    fun loadCurrentAuthorizedAccount() {
        launch {
            runCatchingWithAuth {
                fetchCurrentAuthorizedAccountUseCase.execute()
                    .collect { (account, domain) ->
                        setCurrentAuthorizedAccount(account, domain)
                    }
            }
        }

        launch {
            runCatchingWithAuth {
                fetchAllAuthorizedAccountsUseCase.execute()
                    .collect { accounts ->
                        setOthersAuthorizedAccount(accounts)
                    }
            }
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

    private fun execute(timeline: Timeline, status: Status, action: StatusAction) {
        val index = uiModel.value.timelines.indexOfFirst { it.timeline == timeline }

        val job = launchLazy {
            runCatchingWithAuth { executeStatusActionUseCase.execute(status, action) }
                .onSuccess { setStatus(index, status) }
        }

        setJob(index, job)
        job.start()
    }

    private fun setCurrentAuthorizedAccount(account: Account, domain: Domain) {
        val currentAuthorizedAccount = uiModel.value.account

        mutableUiModel.value = uiModel.value.copy(
            account = currentAuthorizedAccount.copy(
                current = account,
                domain = domain,
            ),
        )
    }

    private fun setOthersAuthorizedAccount(account: Account) {
        val currentAuthorizedAccount = uiModel.value.account
        val nextOthers = currentAuthorizedAccount.others.filterNot {
            it.id == account.id
        } + account

        mutableUiModel.value = uiModel.value.copy(
            account = currentAuthorizedAccount.copy(
                others = nextOthers,
            ),
        )
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

    data class CurrentAuthorizedAccount(
        val current: Account? = null,
        val domain: Domain? = null,
        val others: List<Account> = listOf(),
    )

    data class TimelineState(
        val timeline: Timeline,
        val jobs: List<Job> = listOf(),
        val latestEvent: StreamEvent? = null,
        val scrollToTop: Boolean = false,
        val foreground: Boolean = false,
    )

    companion object Provider {
        private const val UI_MODEL_KEEPER = "TimelinesViewModel.UiModel"

        @Composable
        operator fun invoke(
            component: KoinComponent,
        ): TimelinesViewModel {
            val coroutineScope = rememberCoroutineScope()

            return remember {
                TimelinesViewModel(
                    component.get(),
                    component.get(),
                    component.get(),
                    component.get(),
                    component.get(),
                    component.get(),
                    coroutineScope = coroutineScope,
                )
            }
        }
    }
}
