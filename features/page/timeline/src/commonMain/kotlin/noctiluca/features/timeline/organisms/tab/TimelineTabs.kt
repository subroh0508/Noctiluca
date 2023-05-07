package noctiluca.features.timeline.organisms.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.features.components.atoms.tab.PrimaryTabs
import noctiluca.features.timeline.LocalTimelineListState
import noctiluca.features.timeline.getString
import noctiluca.timeline.domain.model.Timeline

@Composable
internal fun TimelineTabs(
    modifier: Modifier = Modifier,
) {
    val timelineListState = LocalTimelineListState.current

    if (timelineListState.value.isEmpty()) {
        return
    }

    PrimaryTabs(
        timelineListState.value,
        selectedTabIndex = timelineListState.currentTabIndex,
        onClick = { index, _ -> timelineListState.setForeground(index) },
        transform = { (timeline) -> timeline.label() },
        modifier = modifier,
    )
}

@Composable
private fun Timeline.label() = when (this) {
    is Timeline.Global -> getString().timeline_global
    is Timeline.Local -> getString().timeline_local
    is Timeline.Home -> getString().timeline_home
    is Timeline.HashTag -> hashtag.value
    is Timeline.List -> list.title
}
