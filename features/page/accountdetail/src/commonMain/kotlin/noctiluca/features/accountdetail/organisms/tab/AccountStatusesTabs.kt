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

@Composable
private fun TabTitles() = listOf(
    getString().account_detail_tab_statuses,
    getString().account_detail_tab_statuses_and_replies,
    getString().account_detail_tab_media,
)

@Composable
fun AccountStatusesTabs(
    statuses: AccountStatuses,
) = TabRow(
    selectedTabIndex = statuses.tab.ordinal,
) {
    TabTitles().forEachIndexed { i, tab ->
        Tab(
            i == statuses.tab.ordinal,
            onClick = {},
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(48.dp),
            ) { Text(tab) }
        }
    }
}
