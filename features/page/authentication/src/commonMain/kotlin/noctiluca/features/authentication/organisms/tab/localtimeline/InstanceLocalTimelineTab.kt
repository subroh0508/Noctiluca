package noctiluca.features.authentication.organisms.tab.localtimeline

import androidx.compose.foundation.lazy.LazyListScope
import noctiluca.features.authentication.state.LocalTimelineState
import noctiluca.features.components.molecules.list.infiniteScrollFooter
import noctiluca.features.components.molecules.list.items
import noctiluca.features.shared.status.Status
import noctiluca.instance.model.Instance

internal fun LazyListScope.InstanceLocalTimelineTab(
    instance: Instance,
    localTimelineState: LocalTimelineState,
) {
    items(
        localTimelineState.value,
        key = { _, status -> status.id.value },
        showDivider = true,
    ) { _, status ->
        Status(
            status,
            onClickAction = {},
        )
    }

    infiniteScrollFooter(
        isLoading = true,
        onLoad = { localTimelineState.loadMore(this, instance.domain) },
    )
}
