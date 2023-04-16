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

@Composable
private fun TabTitles() = listOf(
    getString().account_detail_tab_statuses,
    getString().account_detail_tab_statuses_and_replies,
    getString().account_detail_tab_media,
)

@Composable
fun AccountDetailContentTabs(
    selectedTabIndex: Int,
) = TabRow(
    selectedTabIndex = selectedTabIndex,
) {
    TabTitles().forEachIndexed { i, tab ->
        Tab(
            i == selectedTabIndex,
            onClick = {},
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(48.dp),
            ) { Text(tab) }
        }
    }
}
