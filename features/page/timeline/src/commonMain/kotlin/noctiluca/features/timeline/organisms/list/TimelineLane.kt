package noctiluca.features.timeline.organisms.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import noctiluca.features.shared.atoms.list.footer
import noctiluca.features.shared.atoms.list.items
import noctiluca.features.shared.status.Action
import noctiluca.features.shared.status.Status
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.model.timeline.Timeline

@Composable
internal fun TimelineLane(
    timelineState: TimelinesViewModel.TimelineState,
    loadState: LoadState?,
    onLoad: suspend CoroutineScope.() -> Unit,
    onClickStatus: CoroutineScope.(StatusId) -> Unit,
    onClickAvatar: CoroutineScope.(AccountId) -> Unit,
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
        modifier = modifier,
        state = lazyListState,
    ) {
        items(
            timelineState.timeline.statuses,
            key = { _, item -> item.id.value },
            showDivider = true,
        ) { _, item ->
            Status(
                item,
                onClickAvatar = onClickAvatar,
                onClick = onClickStatus,
                onClickAction = { onExecuteAction(timelineState.timeline, item, it) },
            )
        }

        footer { TimelineFooter(timelineState, loadState, onLoad) }
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
