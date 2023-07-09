package noctiluca.features.accountdetail.templates.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.organisms.tab.AccountStatusesTabs
import noctiluca.features.accountdetail.organisms.tab.rememberTabbedAccountStatusesState
import noctiluca.features.accountdetail.organisms.topappbar.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.templates.scaffold.accountdetail.AccountDetailCaption
import noctiluca.features.accountdetail.templates.scaffold.accountdetail.StatuseTab
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.components.molecules.scaffold.HeadlineAvatar
import noctiluca.features.components.molecules.scaffold.HeadlineHeader
import noctiluca.features.components.molecules.scaffold.LoadStateLargeHeadlinedScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    viewModel: AccountDetailViewModel,
) {
    LaunchedEffect(viewModel.id) {
        viewModel.load()
        viewModel.refreshStatuses()
    }

    val uiModel by viewModel.uiModel.subscribeAsState()
    val statusesScrollState = rememberTabbedAccountStatusesState(uiModel.tab)

    LoadStateLargeHeadlinedScaffold<AccountAttributes>(
        uiModel.account,
        statusesScrollState.lazyListState,
        tabComposeIndex = 1,
        topAppBar = { scrollBehavior, _, attributes ->
            AccountHeaderTopAppBar(
                attributes,
                scrollBehavior,
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
                uiModel.tab,
                statusesScrollState,
                onSwitch = { tab ->
                    statusesScrollState.cacheScrollPosition(tab)
                    viewModel.switch(tab)
                },
            )
        },
    ) { attributes, tabs, horizontalPadding ->
        item {
            AccountDetailCaption(
                attributes,
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )
        }
        StatuseTab(
            tabs,
            uiModel.foreground,
            loadMore = { viewModel.loadStatusesMore() },
        )
    }
}
