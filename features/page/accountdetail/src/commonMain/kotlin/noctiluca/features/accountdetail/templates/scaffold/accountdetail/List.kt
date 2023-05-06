package noctiluca.features.accountdetail.templates.scaffold.accountdetail

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import noctiluca.features.accountdetail.state.AccountStatusesState
import noctiluca.features.components.molecules.list.infiniteScrollFooter
import noctiluca.features.components.molecules.list.items
import noctiluca.features.shared.status.Status

@Suppress("FunctionNaming")
internal fun LazyListScope.StatuseTab(
    tabs: @Composable () -> Unit,
    statuses: AccountStatusesState,
) {
    item { tabs() }
    items(
        statuses.value.foreground,
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
            if (statuses.value.foreground.isNotEmpty()) {
                statuses.loadMore(this)
            }
        },
    )
}
