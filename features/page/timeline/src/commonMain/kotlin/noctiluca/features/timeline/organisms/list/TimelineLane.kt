package noctiluca.features.timeline.organisms.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.features.shared.status.Status
import noctiluca.features.timeline.state.rememberTimelineStatus

@Composable
internal fun TimelineLane(
    modifier: Modifier = Modifier,
) {
    val timeline = rememberTimelineStatus()
    val statuses = timeline.findForeground()?.timeline?.statuses ?: listOf()

    LazyColumn(
        statuses,
        key = { it.id.value },
        modifier = modifier,
        state = rememberLazyListState(),
        showDivider = true,
        footerContent = {
            TimelineFooter(
                timeline.findForeground()?.deferred?.isActive != false,
            )
        },
    ) { _, item ->
        Status(
            item,
            onClickAction = {},
        )
    }
}

@Composable
private fun TimelineFooter(
    showIndicator: Boolean,
    height: Dp = 64.dp,
) {
    if (!showIndicator) {
        Spacer(Modifier.height(height))
        return
    }

    LaunchedEffect(Unit) {

    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(height),
    ) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
}
