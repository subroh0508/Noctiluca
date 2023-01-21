package noctiluca.features.timeline.state

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.components.state.AuthorizedComposeState
import noctiluca.features.components.state.rememberAuthorizedComposeState
import noctiluca.features.components.state.runCatchingWithAuth
import noctiluca.features.timeline.LocalScope
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.domain.usecase.UpdateTimelineUseCase
import org.koin.core.scope.Scope

internal data class TimelineState(
    val timeline: Timeline,
    val jobs: List<Job> = listOf(),
    val foreground: Boolean = false,
)

internal class TimelineListState(
    private val timelineList: MutableState<List<TimelineState>>,
    private val state: AuthorizedComposeState,
    private val updateTimelineUseCase: UpdateTimelineUseCase,
) : State<List<TimelineState>> by timelineList, AuthorizedComposeState by state {
    constructor(
        state: AuthorizedComposeState = AuthorizedComposeState(),
        koinScope: Scope = getKoinRootScope(),
        timelineList: List<TimelineState> = listOf(
            TimelineState(Timeline.Local(listOf(), onlyMedia = false), foreground = true),
            TimelineState(Timeline.Home(listOf())),
            TimelineState(Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false)),
        ),
    ) : this(mutableStateOf(timelineList), state, koinScope.get())

    fun findForeground() = value.find { it.foreground }
    fun setForeground(index: Int) {
        timelineList.value = value.mapIndexed { i, state ->
            state.copy(foreground = i == index)
        }
    }

    suspend fun loadAll(scope: CoroutineScope) {
        value.forEach { (timeline) -> load(scope, timeline) }
    }

    suspend fun load(scope: CoroutineScope, timeline: Timeline) {
        val index = value.indexOfFirst { it.timeline == timeline }

        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatchingWithAuth { updateTimelineUseCase.execute(timeline) }
                .onSuccess { setTimeline(index, it) }
                .onFailure { it.printStackTrace() }
        }

        setJob(index, job)
        job.start()
    }

    private fun setTimeline(index: Int, timeline: Timeline) {
        set(index) { copy(timeline = timeline, jobs = jobs.filterNot { it.isCompleted }) }
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

    LaunchedEffect(Unit) { timeline.loadAll(this) }

    return timeline
}
