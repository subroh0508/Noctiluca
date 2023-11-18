package noctiluca.features.authentication.organisms.tab.localtimeline

import androidx.compose.foundation.lazy.LazyListScope
import noctiluca.features.shared.molecules.list.infiniteScrollFooter
import noctiluca.features.shared.molecules.list.items
import noctiluca.features.shared.status.Status
import noctiluca.model.status.Status

@Suppress("FunctionNaming")
internal fun LazyListScope.InstanceLocalTimelineTab(
    statuses: List<Status>,
    loadMore: () -> Unit,
) {
    items(
        statuses,
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
        onLoad = { loadMore() },
    )
}
