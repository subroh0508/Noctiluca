package noctiluca.features.accountdetail.organisms.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account
import noctiluca.features.accountdetail.organisms.topappbar.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.state.rememberAccountDetail
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.model.AccountId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    id: AccountId,
) {
    val detail = rememberAccountDetail(id)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            AccountHeaderTopAppBar(
                detail.value.attributes,
                scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        AsyncImage(detail.value.attributes?.header)

        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(50) {
                Text(
                    "item $it",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
