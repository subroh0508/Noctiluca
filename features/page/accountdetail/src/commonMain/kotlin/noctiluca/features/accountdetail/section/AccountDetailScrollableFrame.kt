package noctiluca.features.accountdetail.section

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.component.headline.Avatar
import noctiluca.features.accountdetail.component.headline.Header
import noctiluca.features.accountdetail.section.scrollableframe.AccountDetailScrollableFrameState
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailScrollableFrame(
    paddingValues: PaddingValues,
    attributes: AccountAttributes?,
    relationships: Relationships,
    scrollableFrameState: AccountDetailScrollableFrameState,
    scrollBehavior: TopAppBarScrollBehavior,
    caption: @Composable (AccountAttributes) -> Unit,
    tabs: @Composable (AccountDetailScrollableFrameState) -> Unit,
    content: LazyListScope.() -> Unit,
) {
    attributes ?: return

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollableFrameState.lazyListState,
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                top = paddingValues.calculateTopPadding(),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                bottom = 64.dp,
            ),
            modifier = Modifier.fillMaxSize(),
        ) {
            item { caption(attributes) }
            item { tabs(scrollableFrameState) }

            content()
        }

        AccountDetailHeadline(
            attributes,
            relationships,
            scrollBehavior,
        )

        if (scrollableFrameState.firstVisibleItemIndex >= 1) {
            Box(
                modifier = Modifier.offset(y = 64.dp),
            ) { tabs(scrollableFrameState) }
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
