package noctiluca.features.accountdetail.template

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.accountdetail.component.AccountStatusesScrollState
import noctiluca.features.accountdetail.component.AccountStatusesTabs
import noctiluca.features.accountdetail.component.rememberTabbedAccountStatusesState
import noctiluca.features.accountdetail.component.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.template.accountdetail.AccountDetailCaption
import noctiluca.features.accountdetail.template.accountdetail.StatuseTab
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.navigation.navigateToStatusDetail
import noctiluca.features.shared.molecules.list.LazyColumn
import noctiluca.features.shared.molecules.scaffold.*
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    viewModel: AccountDetailViewModel,
) {
    val uiModel by viewModel.uiModel.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            AccountHeaderTopAppBar(
                uiModel.account,
                scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        when (val state = uiModel) {
            is AccountDetailViewModel.UiModel.Loading -> LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = paddingValues.calculateTopPadding()),
            )

            is AccountDetailViewModel.UiModel.Loaded -> AccountDetailContent(
                tabs = { statusesScrollState ->
                    AccountStatusesTabs(
                        state.query,
                        statusesScrollState,
                        onSwitch = { tab ->
                            statusesScrollState.cacheScrollPosition(tab)
                            viewModel.switch(tab)
                        },
                    )
                },
                paddingValues,
                state.account,
                state.query,
                state.foreground,
                scrollBehavior,
                loadMore = { viewModel.loadStatusesMore() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailContent(
    tabs: @Composable (AccountStatusesScrollState) -> Unit,
    paddingValues: PaddingValues,
    account: AccountAttributes,
    tab: StatusesQuery,
    statuses: List<Status>,
    scrollBehavior: TopAppBarScrollBehavior,
    loadMore: () -> Unit,
) {
    val navigator = LocalNavigator.current
    val statusesScrollState = rememberTabbedAccountStatusesState(tab)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = statusesScrollState.lazyListState,
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                top = paddingValues.calculateTopPadding(),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                bottom = 64.dp,
            ),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                AccountDetailCaption(
                    account,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            StatuseTab(
                tabs = { tabs(statusesScrollState) },
                statuses,
                onClickStatus = { navigator?.navigateToStatusDetail(it) },
                loadMore = loadMore,
            )
        }

        HeadlineHeader(
            account.header,
            scrollBehavior,
        )

        HeadlineAvatar(
            account.avatar,
            scrollBehavior,
        )

        if (statusesScrollState.lazyListState.firstVisibleItemIndex >= 1) {
            Box(
                modifier = Modifier.offset(y = 64.dp),
            ) { tabs(statusesScrollState) }
        }
    }
}

private val AccountDetailViewModel.UiModel.account: AccountAttributes?
    get() = when (this) {
        is AccountDetailViewModel.UiModel.Loaded -> account
        else -> null
    }
