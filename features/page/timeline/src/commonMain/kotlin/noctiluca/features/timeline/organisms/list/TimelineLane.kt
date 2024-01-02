package noctiluca.features.timeline.organisms.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.molecules.list.LazyColumn
import noctiluca.features.shared.status.Action
import noctiluca.features.shared.status.Status
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.model.status.Status
import noctiluca.model.timeline.Timeline

@Composable
internal fun TimelineLane(
    timelineState: TimelinesViewModel.TimelineState,
    loadState: LoadState?,
    onLoad: suspend CoroutineScope.() -> Unit,
    onExecuteAction: CoroutineScope.(Timeline, Status, Action) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
) {
    if (!timelineState.foreground) {
        return
    }

    LaunchedEffect(timelineState.latestEvent) {
        if (lazyListState.firstVisibleItemIndex > 1 || lazyListState.firstVisibleItemScrollOffset != 0) {
            return@LaunchedEffect
        }

        lazyListState.scrollToItem(0)
    }

    LazyColumn(
        timelineState.timeline.statuses,
        key = { it.id.value },
        modifier = modifier,
        state = lazyListState,
        showDivider = true,
        footerContent = { TimelineFooter(timelineState, loadState, onLoad) },
    ) { _, item ->
        Status(
            item,
            onClickAction = { onExecuteAction(timelineState.timeline, item, it) },
        )
    }
}

@Composable
private fun TimelineFooter(
    foreground: TimelinesViewModel.TimelineState,
    loadState: LoadState?,
    onLoad: suspend CoroutineScope.() -> Unit,
    height: Dp = 64.dp,
) {
    if (loadState?.loading == false) {
        Spacer(Modifier.height(height))
        return
    }

    LaunchedEffect(Unit) {
        if (foreground.timeline.statuses.isEmpty()) {
            return@LaunchedEffect
        }
        onLoad()
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(height),
    ) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
}
