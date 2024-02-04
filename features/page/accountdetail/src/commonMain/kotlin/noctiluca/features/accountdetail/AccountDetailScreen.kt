package noctiluca.features.accountdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.accountdetail.component.AccountDetailTabs
import noctiluca.features.accountdetail.component.AccountStatusesScrollState
import noctiluca.features.accountdetail.component.AccountStatusesTab
import noctiluca.features.accountdetail.component.rememberTabbedAccountStatusesState
import noctiluca.features.accountdetail.section.AccountDetailCaption
import noctiluca.features.accountdetail.section.AccountDetailHeadline
import noctiluca.features.accountdetail.section.AccountDetailTopAppBar
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.features.navigation.navigateToStatusDetail
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.shared.molecules.list.LazyColumn
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status
import org.koin.core.parameter.parametersOf
import noctiluca.features.navigation.AccountDetail as NavigationAccountDetailScreen

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureAccountDetailScreenModule = screenModule {
    register<NavigationAccountDetailScreen> { provider ->
        AccountDetailScreen(provider.id)
    }
}

internal data class AccountDetailScreen(
    private val id: String,
) : Screen {
    override val key = "AccountDetail#$id"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) {
        val accountDetailViewModel: AccountDetailViewModel = getScreenModel {
            parametersOf(AccountId(id))
        }
        val accountRelationshipsViewModel: AccountRelationshipsViewModel = getScreenModel {
            parametersOf(AccountId(id))
        }

        AccountDetailScaffold(
            accountDetailViewModel,
            topBar = { account, scrollBehavior ->
                AccountDetailTopAppBar(
                    account,
                    accountRelationshipsViewModel,
                    scrollBehavior,
                )
            },
        ) { uiModel, paddingValues, scrollBehavior ->
            AccountDetailContent(
                tabs = { statusesScrollState ->
                    AccountDetailTabs(
                        uiModel.query,
                        statusesScrollState,
                        onSwitch = { tab ->
                            statusesScrollState.cacheScrollPosition(tab)
                            accountDetailViewModel.switch(tab)
                        },
                    )
                },
                paddingValues,
                accountRelationshipsViewModel,
                uiModel.account,
                uiModel.query,
                uiModel.foreground,
                scrollBehavior,
                loadMore = { accountDetailViewModel.loadStatusesMore() },
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AccountDetailScaffold(
        viewModel: AccountDetailViewModel,
        topBar: @Composable (AccountAttributes?, TopAppBarScrollBehavior) -> Unit,
        content: @Composable (AccountDetailViewModel.UiModel.Loaded, PaddingValues, TopAppBarScrollBehavior) -> Unit,
    ) {
        val uiModel by viewModel.uiModel.collectAsState()

        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = { topBar(uiModel.account, scrollBehavior) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        ) { paddingValues ->
            when (val state = uiModel) {
                is AccountDetailViewModel.UiModel.Loading -> LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .offset(y = paddingValues.calculateTopPadding()),
                )

                is AccountDetailViewModel.UiModel.Loaded -> content(
                    state,
                    paddingValues,
                    scrollBehavior,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AccountDetailContent(
        tabs: @Composable (AccountStatusesScrollState) -> Unit,
        paddingValues: PaddingValues,
        viewModel: AccountRelationshipsViewModel,
        account: AccountAttributes,
        tab: StatusesQuery,
        statuses: List<Status>,
        scrollBehavior: TopAppBarScrollBehavior,
        loadMore: () -> Unit,
    ) {
        val uiModel by viewModel.uiModel.collectAsState()

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
                        uiModel,
                        follow = { viewModel.follow() },
                        block = { viewModel.block() },
                        notifyNewStatus = { viewModel.toggleNotify() },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                AccountStatusesTab(
                    tabs = { tabs(statusesScrollState) },
                    statuses,
                    onClickStatus = { navigator?.navigateToStatusDetail(it) },
                    loadMore = loadMore,
                )
            }

            AccountDetailHeadline(
                account,
                uiModel.relationships,
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
}
