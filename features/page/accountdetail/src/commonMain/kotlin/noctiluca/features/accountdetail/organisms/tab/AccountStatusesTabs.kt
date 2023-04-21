package noctiluca.features.accountdetail.organisms.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.state.AccountStatuses
import noctiluca.features.accountdetail.state.AccountStatusesState

@Composable
fun TabTitles() = listOf(
    AccountStatuses.Tab.STATUSES to getString().account_detail_tab_statuses,
    AccountStatuses.Tab.STATUSES_AND_REPLIES to getString().account_detail_tab_statuses_and_replies,
    AccountStatuses.Tab.MEDIA to getString().account_detail_tab_media,
)

@Composable
internal fun AccountStatusesTabs(
    tabTitles: List<Pair<AccountStatuses.Tab, String>>,
    state: AccountStatusesState,
    onClickTab: (AccountStatuses.Tab, AccountStatuses.Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statuses = state.value

    TabRow(
        selectedTabIndex = statuses.tab.ordinal,
        modifier = modifier,
    ) {
        tabTitles.forEach { (tab, title) ->
            Tab(
                tab == statuses.tab,
                onClick = {
                    onClickTab(statuses.tab, tab)
                    state.switch(tab)
                },
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(48.dp),
                ) { Text(title) }
            }
        }
    }
}
