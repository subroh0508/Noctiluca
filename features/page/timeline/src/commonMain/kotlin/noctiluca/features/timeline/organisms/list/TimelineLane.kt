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
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.features.shared.status.Status
import noctiluca.features.timeline.state.TimelineState
import noctiluca.timeline.domain.model.Timeline

@Composable
internal fun TimelineLane(
    timelineState: TimelineState,
    onLoad: suspend CoroutineScope.(Timeline) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
) {
    if (!timelineState.foreground) {
        return
    }

    LazyColumn(
        timelineState.timeline.statuses,
        key = { it.id.value },
        modifier = modifier,
        state = lazyListState,
        showDivider = true,
        footerContent = { TimelineFooter(timelineState, onLoad) },
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
    onLoad: suspend CoroutineScope.(Timeline) -> Unit,
    height: Dp = 64.dp,
) {
    if (foreground.jobs.isEmpty()) {
        Spacer(Modifier.height(height))
        return
    }

    LaunchedEffect(Unit) {
        if (foreground.timeline.statuses.isEmpty()) {
            return@LaunchedEffect
        }
        onLoad(foreground.timeline)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(height),
    ) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
}
