package noctiluca.features.accountdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.accountdetail.component.AccountDetailTabs
import noctiluca.features.accountdetail.model.AttributesModel
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.features.accountdetail.section.AccountDetailCaption
import noctiluca.features.accountdetail.section.AccountDetailScrollableFrame
import noctiluca.features.accountdetail.section.AccountDetailTopAppBar
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.features.accountdetail.viewmodel.AccountStatusesViewModel
import noctiluca.features.navigation.navigateToStatusDetail
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.shared.molecules.list.infiniteScrollFooter
import noctiluca.features.shared.molecules.list.items
import noctiluca.features.shared.status.Status
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
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
        val viewModel: AccountRelationshipsViewModel = getScreenModel {
            parametersOf(AccountId(id))
        }
        val uiModel by viewModel.uiModel.collectAsState()

        AccountDetailScaffold(
            uiModel,
            topBar = { account, scrollBehavior ->
                AccountDetailTopAppBar(
                    account,
                    uiModel,
                    scrollBehavior,
                    mute = { viewModel.mute() },
                    block = { viewModel.block() },
                    report = {},
                    toggleReblogs = { viewModel.toggleReblogs() },
                )
            },
        ) { attributesUiModel, paddingValues, scrollBehavior ->
            AccountDetailContent(
                paddingValues,
                attributesUiModel,
                uiModel,
                scrollBehavior,
                follow = { viewModel.follow() },
                block = { viewModel.block() },
                notifyNewStatus = { viewModel.toggleNotify() },
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AccountDetailScaffold(
        relationshipsModel: RelationshipsModel,
        topBar: @Composable (AccountAttributes?, TopAppBarScrollBehavior) -> Unit,
        content: @Composable (AttributesModel, PaddingValues, TopAppBarScrollBehavior) -> Unit,
    ) {
        val viewModel: AccountDetailViewModel = getScreenModel {
            parametersOf(AccountId(id))
        }
        val uiModel by viewModel.uiModel.collectAsState()

        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = { topBar(uiModel.attributes, scrollBehavior) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        ) { paddingValues ->
            if (showProgress(uiModel, relationshipsModel)) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .offset(y = paddingValues.calculateTopPadding()),
                )
            }

            content(
                uiModel,
                paddingValues,
                scrollBehavior,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AccountDetailContent(
        paddingValues: PaddingValues,
        attributesModel: AttributesModel,
        relationshipsModel: RelationshipsModel,
        scrollBehavior: TopAppBarScrollBehavior,
        follow: () -> Unit,
        block: () -> Unit,
        notifyNewStatus: () -> Unit,
    ) {
        val viewModel: AccountStatusesViewModel = getScreenModel {
            parametersOf(AccountId(id))
        }
        val uiModel by viewModel.uiModel.collectAsState()

        val navigator = LocalNavigator.current

        AccountDetailScrollableFrame(
            paddingValues,
            attributesModel.attributes,
            relationshipsModel.relationships,
            uiModel.query,
            scrollBehavior,
            caption = { attributes ->
                AccountDetailCaption(
                    attributes,
                    relationshipsModel,
                    follow = follow,
                    block = block,
                    notifyNewStatus = notifyNewStatus,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            },
            tabs = { scrollState ->
                AccountDetailTabs(
                    uiModel.query,
                    scrollState,
                    onSwitch = { tab ->
                        scrollState.cacheScrollPosition(tab)
                        viewModel.switch(tab)
                    },
                )
            },
        ) {
            items(
                uiModel.foreground,
                key = { _, status -> status.id.value },
                showDivider = true,
            ) { _, status ->
                Status(
                    status,
                    onClick = { navigator?.navigateToStatusDetail(it) },
                    onClickAvatar = {},
                    onClickAction = {},
                )
            }

            infiniteScrollFooter(
                isLoading = uiModel.state.loading,
                onLoad = { viewModel.loadStatusesMore() },
            )
        }
    }

    private fun showProgress(
        attributesModel: AttributesModel,
        relationshipsModel: RelationshipsModel,
    ) = attributesModel.attributes == null ||
            attributesModel.state.loading ||
            relationshipsModel.state.loading
}
