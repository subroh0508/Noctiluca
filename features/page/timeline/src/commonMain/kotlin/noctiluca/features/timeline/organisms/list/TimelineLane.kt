package noctiluca.features.timeline.organisms.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.features.shared.status.Status
import noctiluca.features.timeline.LocalTimelineListState
import noctiluca.features.timeline.state.TimelineListState
import noctiluca.features.timeline.state.TimelineState

@Composable
internal fun TimelineLane(
    timelineListState: TimelineListState = LocalTimelineListState.current,
    modifier: Modifier = Modifier,
) {
    val foreground = timelineListState.findForeground() ?: return

    LazyColumn(
        foreground.timeline.statuses,
        key = { it.id.value },
        modifier = modifier,
        state = rememberLazyListState(),
        showDivider = true,
        footerContent = { TimelineFooter(foreground) },
    ) { _, item ->
        Status(
            item,
            onClickAction = {},
        )
    }
}

@Composable
private fun TimelineFooter(
    foreground: TimelineState,
    height: Dp = 64.dp,
) {
    val timeline = LocalTimelineListState.current

    if (foreground.jobs.isEmpty()) {
        Spacer(Modifier.height(height))
        return
    }

    LaunchedEffect(Unit) {
        if (foreground.timeline.statuses.isEmpty()) {
            return@LaunchedEffect
        }

        timeline.load(this, foreground.timeline)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(height),
    ) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
}
