package noctiluca.features.timeline.state

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.components.state.AuthorizedComposeState
import noctiluca.features.components.state.rememberAuthorizedComposeState
import noctiluca.features.components.state.runCatchingWithAuth
import noctiluca.features.timeline.LocalScope
import noctiluca.status.model.Status
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.domain.usecase.ExecuteStatusActionUseCase
import noctiluca.timeline.domain.usecase.FetchTimelineStreamUseCase
import noctiluca.timeline.domain.usecase.UpdateTimelineUseCase
import noctiluca.timeline.model.StreamEvent
import org.koin.core.scope.Scope

internal data class TimelineState(
    val timeline: Timeline,
    val jobs: List<Job> = listOf(),
    val latestEvent: StreamEvent? = null,
    val scrollToTop: Boolean = false,
    val foreground: Boolean = false,
)

@Suppress("TooManyFunctions")
internal class TimelineListState(
    private val timelineList: MutableState<List<TimelineState>>,
    private val state: AuthorizedComposeState,
    private val fetchTimelineStreamUseCase: FetchTimelineStreamUseCase,
    private val updateTimelineUseCase: UpdateTimelineUseCase,
    private val executeStatusActionUseCase: ExecuteStatusActionUseCase,
) : State<List<TimelineState>> by timelineList, AuthorizedComposeState by state {
    constructor(
        state: AuthorizedComposeState = AuthorizedComposeState(),
        koinScope: Scope = getKoinRootScope(),
        timelineList: List<TimelineState> = listOf(
            TimelineState(Timeline.Local(listOf(), onlyMedia = false), foreground = true),
            TimelineState(Timeline.Home(listOf())),
            TimelineState(Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false)),
        ),
    ) : this(
        mutableStateOf(timelineList),
        state,
        koinScope.get(),
        koinScope.get(),
        koinScope.get(),
    )

    fun findForeground() = value.find { it.foreground }
    fun setForeground(index: Int) {
        if (value[index].foreground) {
            set(index) { copy(scrollToTop = true) }
            return
        }

        timelineList.value = value.mapIndexed { i, state ->
            state.copy(foreground = i == index)
        }
    }

    fun scrolledToTop(index: Int) {
        set(index) { copy(scrollToTop = false) }
    }

    fun subscribeAll(scope: CoroutineScope) {
        value.forEachIndexed { index, (timeline) -> subscribe(scope, index, timeline) }
    }

    fun loadAll(scope: CoroutineScope) {
        value.forEach { (timeline) -> load(scope, timeline) }
    }

    fun load(scope: CoroutineScope, timeline: Timeline) {
        val index = value.indexOfFirst { it.timeline == timeline }

        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatchingWithAuth { updateTimelineUseCase.execute(timeline) }
                .onSuccess { setTimeline(index, it) }
                .onFailure { it.printStackTrace() }
        }

        setJob(index, job)
        job.start()
    }

    fun favourite(scope: CoroutineScope, timeline: Timeline, status: Status) = execute(scope, timeline, status, StatusAction.FAVOURITE)
    fun boost(scope: CoroutineScope, timeline: Timeline, status: Status) = execute(scope, timeline, status, StatusAction.BOOST)
    fun bookmark(scope: CoroutineScope, timeline: Timeline, status: Status) = execute(scope, timeline, status, StatusAction.BOOKMARK)

    private fun execute(scope: CoroutineScope, timeline: Timeline, status: Status, action: StatusAction) {
        val index = value.indexOfFirst { it.timeline == timeline }

        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatchingWithAuth { executeStatusActionUseCase.execute(status, action) }
                .onSuccess { setStatus(index, status) }
        }

        setJob(index, job)
        job.start()
    }

    private fun subscribe(scope: CoroutineScope, index: Int, timeline: Timeline) {
        scope.launch {
            runCatchingWithAuth {
                fetchTimelineStreamUseCase.execute(timeline)
                    .collect { receiveEvent(index, it) }
            }
        }
    }

    private fun receiveEvent(index: Int, event: StreamEvent) {
        val current = value[index]

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
        val current = timelineList.value.toMutableList()
        current[index] = current[index].block()
        timelineList.value = current
    }
}

@Composable
internal fun rememberTimelineStatus(
    scope: Scope = LocalScope.current,
): TimelineListState {
    val authorizationState = rememberAuthorizedComposeState()

    val timeline = remember { TimelineListState(authorizationState, scope) }

    LaunchedEffect(Unit) {
        timeline.loadAll(this)
        timeline.subscribeAll(this)
    }

    return timeline
}
