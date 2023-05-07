package noctiluca.features.timeline.organisms.tab

import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    TabRow(
        selectedTabIndex = timelineListState.currentTabIndex,
        modifier = modifier,
    ) {
        timelineListState.value.forEachIndexed { index, (timeline, _, _, _, foreground) ->
            Tab(
                selected = foreground,
                text = { Text(timeline.label()) },
                onClick = { timelineListState.setForeground(index) },
            )
        }
    }
}

@Composable
private fun Timeline.label() = when (this) {
    is Timeline.Global -> getString().timeline_global
    is Timeline.Local -> getString().timeline_local
    is Timeline.Home -> getString().timeline_home
    is Timeline.HashTag -> hashtag.value
    is Timeline.List -> list.title
}
