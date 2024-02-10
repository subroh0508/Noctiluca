package noctiluca.features.accountdetail.component

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.section.scrollableframe.AccountDetailScrollableFrameState
import noctiluca.features.shared.atoms.tab.PrimaryTabs
import noctiluca.model.accountdetail.StatusesQuery

@Composable
internal fun AccountDetailTabs(
    current: StatusesQuery,
    queries: List<StatusesQuery>,
    scrollableFrameState: AccountDetailScrollableFrameState,
    onSwitch: (StatusesQuery) -> Unit = {},
    modifier: Modifier = Modifier,
) = PrimaryTabs(
    queries.mapTitles(),
    current.ordinal,
    onClick = { _, (tab, _) ->
        scrollableFrameState.cacheScrollPosition(current, tab)
        onSwitch(tab)
    },
    transform = { (_, label) -> label },
    modifier = modifier,
)

@Composable
private fun List<StatusesQuery>.mapTitles() = map {
    when (it) {
        StatusesQuery.DEFAULT -> it to getString().account_detail_tab_statuses
        StatusesQuery.WITH_REPLIES -> it to getString().account_detail_tab_statuses_and_replies
        StatusesQuery.ONLY_MEDIA -> it to getString().account_detail_tab_media
    }
}
