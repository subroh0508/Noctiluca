package noctiluca.features.timeline.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import noctiluca.features.navigation.AccountDetail
import noctiluca.features.navigation.StatusDetail
import noctiluca.features.shared.components.list.footer
import noctiluca.features.shared.components.list.items
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.status.Action
import noctiluca.features.shared.status.Status
import noctiluca.features.timeline.model.TimelinesModel
import noctiluca.model.status.Status
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId

@Composable
internal fun TimelineContent(
    timelineId: TimelineId,
    timelineState: TimelinesModel.State,
    loadState: LoadState?,
    lazyListState: LazyListState,
    onLoad: (TimelineId) -> Unit,
    onClickStatus: (Screen) -> Unit,
    onClickAvatar: (Screen) -> Unit,
    onExecuteAction: (Timeline, Status, Action) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            val accountDetail = rememberScreen(AccountDetail(item.tooter.id.value))
            val statusDetail = rememberScreen(StatusDetail(item.id.value))

            Status(
                item,
                onClickAvatar = { onClickAvatar(accountDetail) },
                onClick = { onClickStatus(statusDetail) },
                onClickAction = { onExecuteAction(timelineState.timeline, item, it) },
            )
        }

        footer {
            TimelineFooter(
                timelineState,
                loadState,
                onLoad = { onLoad(timelineId) },
            )
        }
    }
}

@Composable
private fun TimelineFooter(
    foreground: TimelinesModel.State,
    loadState: LoadState?,
    onLoad: () -> Unit,
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
