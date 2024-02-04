package noctiluca.features.accountdetail.section

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.component.AccountStatusesScrollState
import noctiluca.features.accountdetail.component.headline.Avatar
import noctiluca.features.accountdetail.component.headline.Header
import noctiluca.features.accountdetail.component.rememberTabbedAccountStatusesState
import noctiluca.features.shared.molecules.list.LazyColumn
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships
import noctiluca.model.accountdetail.StatusesQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailScrollableFrame(
    paddingValues: PaddingValues,
    attributes: AccountAttributes?,
    relationships: Relationships,
    query: StatusesQuery,
    scrollBehavior: TopAppBarScrollBehavior,
    caption: @Composable (AccountAttributes) -> Unit,
    tabs: @Composable (AccountStatusesScrollState) -> Unit,
    content: LazyListScope.() -> Unit,
) {
    attributes ?: return

    val statusesScrollState = rememberTabbedAccountStatusesState(query)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = statusesScrollState.lazyListState,
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                top = paddingValues.calculateTopPadding(),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                bottom = 64.dp,
            ),
            modifier = Modifier.fillMaxSize(),
        ) {
            item { caption(attributes) }
            item { tabs(statusesScrollState) }

            content()
        }

        AccountDetailHeadline(
            attributes,
            relationships,
            scrollBehavior,
        )

        if (statusesScrollState.lazyListState.firstVisibleItemIndex >= 1) {
            Box(
                modifier = Modifier.offset(y = 64.dp),
            ) { tabs(statusesScrollState) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailHeadline(
    attributes: AccountAttributes,
    relationships: Relationships,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    Header(attributes.header, relationships, scrollBehavior)
    Avatar(attributes.avatar, scrollBehavior)
}
