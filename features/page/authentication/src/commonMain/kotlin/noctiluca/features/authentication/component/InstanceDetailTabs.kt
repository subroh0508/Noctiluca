package noctiluca.features.authentication.component

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.section.scrollableframe.InstanceDetailScrollableFrameState
import noctiluca.features.shared.atoms.tab.PrimaryTabs

enum class InstancesTab {
    INFO, EXTENDED_DESCRIPTION, LOCAL_TIMELINE
}

@Composable
internal fun InstanceDetailTabs(
    scrollState: InstanceDetailScrollableFrameState,
    modifier: Modifier = Modifier,
) {
    if (scrollState.tabs.isEmpty()) {
        return
    }

    PrimaryTabs(
        scrollState.tabs.mapTitles(),
        scrollState.currentIndex,
        onClick = { _, (tab, _) -> scrollState.cacheScrollPosition(tab) },
        transform = { (_, label) -> label },
        modifier = modifier,
    )
}

@Composable
private fun List<Int>.mapTitles() = map {
    when (val tab = InstancesTab.entries[it]) {
        InstancesTab.INFO -> tab to getString().sign_in_instance_detail_tab_info
        InstancesTab.EXTENDED_DESCRIPTION -> tab to getString().sign_in_instance_detail_tab_extended_description
        InstancesTab.LOCAL_TIMELINE -> tab to getString().sign_in_instance_detail_tab_local_timeline
    }
}
