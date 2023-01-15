package noctiluca.features.timeline.organisms.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = modifier,
        state = rememberLazyListState(),
        showDivider = true,
    ) { _, item ->
        Status(
            item,
            onClickAction = {},
        )
    }
}
