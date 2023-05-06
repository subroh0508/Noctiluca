package noctiluca.features.accountdetail.templates.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.organisms.tab.AccountStatusesTabs
import noctiluca.features.accountdetail.organisms.tab.rememberTabbedAccountStatusesState
import noctiluca.features.accountdetail.organisms.topappbar.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.state.rememberAccountDetail
import noctiluca.features.accountdetail.state.rememberAccountStatuses
import noctiluca.features.accountdetail.templates.scaffold.accountdetail.AccountDetailCaption
import noctiluca.features.accountdetail.templates.scaffold.accountdetail.StatuseTab
import noctiluca.features.components.molecules.scaffold.HeadlineAvatar
import noctiluca.features.components.molecules.scaffold.HeadlineHeader
import noctiluca.features.components.molecules.scaffold.LoadStateLargeHeadlinedScaffold
import noctiluca.model.AccountId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    id: AccountId,
    onBackToPreviousScreen: () -> Unit,
) {
    val accountDetailLoadState by rememberAccountDetail(id)
    val statuses = rememberAccountStatuses(id)
    val statusesScrollState = rememberTabbedAccountStatusesState(statuses.value.tab)

    LoadStateLargeHeadlinedScaffold<AccountAttributes>(
        accountDetailLoadState.loadState,
        statusesScrollState.lazyListState,
        tabComposeIndex = 1,
        topAppBar = { scrollBehavior, _, attributes ->
            AccountHeaderTopAppBar(
                attributes,
                scrollBehavior,
                onBackPressed = { onBackToPreviousScreen() },
            )
        },
        header = { scrollBehavior, attributes ->
            HeadlineHeader(
                attributes.header,
                scrollBehavior,
            )
        },
        avatar = { scrollBehavior, attributes ->
            HeadlineAvatar(
                attributes.avatar,
                scrollBehavior,
            )
        },
        tabs = {
            AccountStatusesTabs(
                statuses,
                statusesScrollState,
            )
        },
    ) { attributes, tabs, horizontalPadding ->
        item {
            AccountDetailCaption(
                attributes,
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )
        }
        StatuseTab(tabs, statuses)
    }
}
