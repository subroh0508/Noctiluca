package noctiluca.features.signin.component

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import noctiluca.features.shared.components.tab.PrimaryTabs
import noctiluca.features.signin.getString
import noctiluca.features.signin.section.scrollableframe.InstanceDetailScrollableFrameState

enum class InstancesTab {
    Info, ExtendedDescription, LocalTimeline
}

@Composable
internal fun InstanceDetailTabs(
    current: InstancesTab,
    tabList: List<InstancesTab>,
    scrollState: InstanceDetailScrollableFrameState,
    onSwitch: (InstancesTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tabList.isEmpty()) {
        return
    }

    PrimaryTabs(
        tabList.mapTitles(),
        tabList.indexOf(current),
        onClick = { _, (tab, _) ->
            scrollState.cacheScrollPosition(current, tab)
            onSwitch(tab)
        },
        transform = { (_, label) -> label },
        modifier = modifier,
        tabModifiers = { _, (tab, _) ->
            Modifier.testTag(tab.name)
        }
    )
}

@Composable
private fun List<InstancesTab>.mapTitles() = map {
    when (it) {
        InstancesTab.Info -> it to getString().sign_in_instance_detail_tab_info
        InstancesTab.ExtendedDescription -> it to getString().sign_in_instance_detail_tab_extended_description
        InstancesTab.LocalTimeline -> it to getString().sign_in_instance_detail_tab_local_timeline
    }
}
