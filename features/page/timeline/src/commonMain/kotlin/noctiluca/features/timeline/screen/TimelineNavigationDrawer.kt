package noctiluca.features.timeline.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.features.navigation.AccountDetail
import noctiluca.features.navigation.rememberSignInScreen
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.timeline.TimelineLaneScreen
import noctiluca.features.timeline.model.AuthorizedAccountModel
import noctiluca.features.timeline.template.drawer.header.CurrentAuthorizedAccountHeader
import noctiluca.features.timeline.template.drawer.menu.AuthorizedAccountsList
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenus
import noctiluca.features.timeline.viewmodel.AuthorizedAccountViewModel
import noctiluca.model.Domain
import noctiluca.model.account.Account

@Composable
internal fun TimelineLaneScreen.TimelineNavigationDrawer(
    content: @Composable (Account?, Domain?, DrawerState) -> Unit,
) {
    val viewModel: AuthorizedAccountViewModel = getAuthorizedScreenModel()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val uiModel by viewModel.uiModel.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {
            TimelineDrawerSheet(
                uiModel,
                drawerState,
                onClickOtherAccount = { viewModel.switch(it) },
            )
        },
        drawerState = drawerState,
    ) { content(uiModel.current, uiModel.domain, drawerState) }
}

@Composable
private fun TimelineDrawerSheet(
    model: AuthorizedAccountModel,
    drawerState: DrawerState,
    onClickOtherAccount: (Account) -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        Modifier.fillMaxHeight(),
    ) {
        Spacer(Modifier.height(12.dp))
        DrawerHeader(model.current, drawerState, scope)

        HorizontalDivider()

        Box(Modifier.weight(1F)) {
            AuthorizedAccountsList(
                model.others,
                onClick = {
                    scope.handleOnClick(drawerState) {
                        onClickOtherAccount(it)
                    }
                },
            )
        }

        HorizontalDivider()

        DrawerMenus(drawerState, scope)
    }
}

@Composable
private fun DrawerHeader(
    account: Account?,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    account ?: return
    val navigator = LocalNavigator.current
    val accountDetail = rememberScreen(AccountDetail(account.id.value))

    CurrentAuthorizedAccountHeader(
        account,
        onClickOpenAccountDetail = {
            scope.handleOnClick(drawerState) {
                navigator?.push(accountDetail)
            }
        },
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = 28.dp,
                vertical = 16.dp,
            ),
    )
}

@Composable
private fun DrawerMenus(
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    val navigator = LocalNavigator.current
    val signIn = rememberSignInScreen()

    TimelineDrawerMenus(
        onClickDrawerMenu = { item ->
            scope.handleOnClick(drawerState) {
                when (item) {
                    is TimelineDrawerMenu.NewAccount -> navigator?.push(signIn)
                    is TimelineDrawerMenu.Settings -> Unit
                }
            }
        },
    )
}

private fun CoroutineScope.handleOnClick(
    drawerState: DrawerState,
    onClick: () -> Unit,
) = launch {
    drawerState.close()
    onClick()
}
