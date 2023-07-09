package noctiluca.features.timeline.organisms.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.features.components.atoms.tab.PrimaryTabs
import noctiluca.features.timeline.LocalTimelineListState
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.timeline.domain.model.Timeline

@Composable
internal fun TimelineTabs(
    uiModel: TimelinesViewModel.UiModel,
    onClickTab: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiModel.timelines.isEmpty()) {
        return
    }

    PrimaryTabs(
        uiModel.timelines,
        selectedTabIndex = uiModel.currentTabIndex,
        onClick = { index, _ -> onClickTab(index) },
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
