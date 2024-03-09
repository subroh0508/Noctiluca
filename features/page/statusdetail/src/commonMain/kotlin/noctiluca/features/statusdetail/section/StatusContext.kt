package noctiluca.features.statusdetail.section

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import noctiluca.features.navigation.AccountDetail
import noctiluca.features.navigation.StatusDetail
import noctiluca.features.statusdetail.component.ConversationItem
import noctiluca.features.statusdetail.component.item.Position
import noctiluca.model.StatusId
import noctiluca.model.status.StatusList
import noctiluca.features.statusdetail.component.StatusDetail as ComposableStatusDetail

@Composable
internal fun StatusContext(
    primary: StatusId,
    statuses: StatusList,
    lazyListState: LazyListState,
    paddingValues: PaddingValues,
    onClickStatus: (Screen) -> Unit,
    onClickAvatar: (Screen) -> Unit,
) {
    HorizontalDivider(
        modifier = Modifier.offset(y = paddingValues.calculateTopPadding()),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
            bottom = 64.dp,
        ),
    ) {
        itemsIndexed(
            statuses,
            key = { _, status -> status.id.value },
        ) { index, status ->
            val statusDetail = rememberScreen(StatusDetail(status.id.value))
            val accountDetail = rememberScreen(AccountDetail(status.tooter.id.value))

            if (status.id == primary) {
                ComposableStatusDetail(
                    status,
                    onClickAvatar = { onClickAvatar(accountDetail) },
                    onClickAction = { },
                )
                return@itemsIndexed
            }

            val position = when (index) {
                0 -> Position.TOP
                statuses.size - 1 -> Position.BOTTOM
                else -> Position.MIDDLE
            }

            ConversationItem(
                status,
                position,
                onClickStatus = { onClickStatus(statusDetail) },
                onClickAvatar = { onClickAvatar(accountDetail) },
                onClickAction = { },
            )
        }
    }
}
