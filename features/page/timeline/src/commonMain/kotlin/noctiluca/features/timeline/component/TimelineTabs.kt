package noctiluca.features.timeline.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import noctiluca.features.shared.components.appbar.scrollToTop
import noctiluca.features.shared.components.tab.PrimaryTabs
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.model.TimelinesModel
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineTabs(
    tabs: Map<TimelineId, TimelinesModel.State>,
    currentTabIndex: Int,
    lazyListState: Map<TimelineId, LazyListState>,
    scrollBehavior: TopAppBarScrollBehavior,
    onClickTab: (TimelineId) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tabs.isEmpty()) {
        return
    }

    val scope = rememberCoroutineScope()

    PrimaryTabs(
        tabs.values.toList(),
        selectedTabIndex = currentTabIndex,
        onClick = { index, _ ->
            val timelineId = tabs.keys.toList()[index]
            scope.launch {
                if (tabs[timelineId]?.foreground == true) {
                    lazyListState[timelineId]?.animateScrollToItem(0)
                    scrollBehavior.scrollToTop()
                }

                onClickTab(timelineId)
            }
        },
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
