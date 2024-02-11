package noctiluca.features.authentication.component.tab

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        scrollState.tabs,
        scrollState.currentIndex,
        onClick = { _, (tab, _) -> scrollState.cacheScrollPosition(tab) },
        transform = { (_, label) -> label },
        modifier = modifier,
    )
}
