package noctiluca.features.accountdetail.organisms.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.state.AccountStatuses
import noctiluca.features.accountdetail.state.AccountStatusesState
import noctiluca.features.accountdetail.state.rememberAccountStatuses
import noctiluca.features.shared.status.Status
import noctiluca.model.AccountId
import noctiluca.status.model.Status

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

fun LazyListScope.statuses(
    statuses: AccountStatuses,
) {
    val foreground = statuses.foreground
    items(
        foreground.size,
        key = { foreground[it].id.value },
    ) {
        Status(
            foreground[it],
            onClickAction = { },
        )
    }
}
