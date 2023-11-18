package noctiluca.features.accountdetail.templates.scaffold.accountdetail

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import noctiluca.features.shared.molecules.list.infiniteScrollFooter
import noctiluca.features.shared.molecules.list.items
import noctiluca.features.shared.status.Status
import noctiluca.model.status.Status

@Suppress("FunctionNaming")
internal fun LazyListScope.StatuseTab(
    tabs: @Composable () -> Unit,
    statuses: List<Status>,
    loadMore: () -> Unit,
) {
    item { tabs() }
    items(
        statuses,
        key = { _, status -> status.id.value },
        showDivider = true,
    ) { _, status ->
        Status(
            status,
            onClickAction = { },
        )
    }

    infiniteScrollFooter(
        isLoading = false,
        onLoad = {
            if (statuses.isNotEmpty()) {
                loadMore()
            }
        },
    )
}
