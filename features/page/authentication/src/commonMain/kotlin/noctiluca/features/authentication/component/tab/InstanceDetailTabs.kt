package noctiluca.features.authentication.component.tab

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.authentication.section.InstanceDetailScrollState
import noctiluca.features.shared.atoms.tab.PrimaryTabs

enum class InstancesTab {
    INFO, EXTENDED_DESCRIPTION, LOCAL_TIMELINE
}

@Composable
internal fun InstanceDetailTabs(
    statusesScrollState: InstanceDetailScrollState,
    modifier: Modifier = Modifier,
) {
    if (statusesScrollState.tabs.isEmpty()) {
        return
    }

    PrimaryTabs(
        statusesScrollState.tabs,
        statusesScrollState.currentIndex,
        onClick = { _, (tab, _) -> statusesScrollState.cacheScrollPosition(tab) },
        transform = { (_, label) -> label },
        modifier = modifier,
    )
}
