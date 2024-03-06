package noctiluca.features.timeline.organisms.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.features.shared.atoms.tab.PrimaryTabs
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.model.TimelinesModel
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId

@Composable
internal fun TimelineTabs(
    uiModel: TimelinesModel,
    onClickTab: (TimelineId) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiModel.tabs.isEmpty()) {
        return
    }

    PrimaryTabs(
        uiModel.toTabList(),
        selectedTabIndex = uiModel.currentTabIndex,
        onClick = { index, _ -> onClickTab(uiModel.findTimelineId(index)) },
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
