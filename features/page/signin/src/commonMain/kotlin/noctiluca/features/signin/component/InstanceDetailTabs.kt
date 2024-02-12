package noctiluca.features.signin.component

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.shared.atoms.tab.PrimaryTabs
import noctiluca.features.signin.getString
import noctiluca.features.signin.section.scrollableframe.InstanceDetailScrollableFrameState

enum class InstancesTab {
    INFO, EXTENDED_DESCRIPTION, LOCAL_TIMELINE
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
    )
}

@Composable
private fun List<InstancesTab>.mapTitles() = map {
    when (it) {
        InstancesTab.INFO -> it to getString().sign_in_instance_detail_tab_info
        InstancesTab.EXTENDED_DESCRIPTION -> it to getString().sign_in_instance_detail_tab_extended_description
        InstancesTab.LOCAL_TIMELINE -> it to getString().sign_in_instance_detail_tab_local_timeline
    }
}
