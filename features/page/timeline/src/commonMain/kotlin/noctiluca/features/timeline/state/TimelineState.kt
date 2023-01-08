package noctiluca.features.timeline.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import noctiluca.components.state.AuthorizedComposeState
import noctiluca.components.state.rememberAuthorizedComposeState
import noctiluca.components.state.runCatchingWithAuth
import noctiluca.features.timeline.LocalScope
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.domain.usecase.UpdateTimelineUseCase
import org.koin.core.scope.Scope
import kotlin.coroutines.EmptyCoroutineContext

internal data class TimelineState(
    val timeline: Timeline,
    val deferred: Deferred<Timeline>? = null,
    val foreground: Boolean = false,
)

internal class TimelineListState(
    private val timelineList: MutableState<List<TimelineState>>,
    private val scope: CoroutineScope,
    private val state: AuthorizedComposeState<Timeline>,
) : State<List<TimelineState>> by timelineList, AuthorizedComposeState<Timeline> by state, CoroutineScope by scope {
    constructor(
        scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
        state: AuthorizedComposeState<Timeline> = AuthorizedComposeState(),
        timelineList: List<TimelineState> = listOf(
            TimelineState(Timeline.Local(listOf(), onlyMedia = false), foreground = true),
            TimelineState(Timeline.Home(listOf())),
            TimelineState(Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false)),
        ),
    ) : this(mutableStateOf(timelineList), scope, state)

    private val deferreds: List<Deferred<Timeline>?>
        get() = value.map { it.deferred }

    fun toList() = value.map { it.timeline }
    fun findForeground() = value.find { it.foreground }

    fun setForeground(index: Int) {
        timelineList.value = value.mapIndexed { i, state ->
            state.copy(foreground = i == index)
        }
    }

    fun loadAll(deferreds: List<Deferred<Timeline>>) {
        setDeferreds(deferreds)
        value.forEachIndexed { i, _ -> load(i) }
    }

    fun load(index: Int) {
        val deferred = deferreds[index] ?: return

        launch {
            runCatchingWithAuth { deferred.await() }
                .onSuccess { set(index) { copy(timeline = it, deferred = null) } }
                .onFailure { it.printStackTrace() }
        }
    }

    private fun setDeferreds(deferreds: List<Deferred<Timeline>>) {
        timelineList.value = deferreds.mapIndexed { i, deferred ->
            value[i].deferred?.cancel()
            value[i].copy(deferred = deferred)
        }
    }

    private fun setDeferred(index: Int, deferred: Deferred<Timeline>) {
        if (deferreds[index] != null) {
            return
        }

        set(index) { copy(deferred = deferred) }
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
    val coroutineScope = rememberCoroutineScope()
    val authorizationState = rememberAuthorizedComposeState<Timeline>()
    val updateTimelineUseCase: UpdateTimelineUseCase = remember { scope.get() }

    val timeline = remember { TimelineListState(coroutineScope, authorizationState) }

    LaunchedEffect(Unit) {
        try {
            timeline.loadAll(updateTimelineUseCase.execute(timeline.toList()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return timeline
}
