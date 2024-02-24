package noctiluca.features.timeline.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.features.navigation.AccountDetail
import noctiluca.features.navigation.rememberSignInScreen
import noctiluca.features.timeline.component.drawer.AuthorizedAccountHeader
import noctiluca.features.timeline.component.drawer.AuthorizedAccountList
import noctiluca.features.timeline.model.AuthorizedAccountModel
import noctiluca.features.timeline.component.drawer.DrawerBottomMenu
import noctiluca.features.timeline.component.drawer.DrawerBottomMenus
import noctiluca.model.account.Account

@Composable
internal fun TimelineDrawerSheet(
    model: AuthorizedAccountModel,
    drawerState: DrawerState,
    onClickOtherAccount: (Account) -> Unit,
) = ModalDrawerSheet(
    Modifier.fillMaxHeight(),
) {
    Spacer(Modifier.height(12.dp))
    DrawerHeader(
        model.current,
        drawerState,
    )

    HorizontalDivider()

    DrawerAccountList(
        model.others,
        drawerState,
        onClickOtherAccount,
    )

    HorizontalDivider()

    DrawerMenus(drawerState)
}

@Composable
private fun DrawerHeader(
    account: Account?,
    drawerState: DrawerState,
) {
    account ?: return

    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current
    val accountDetail = rememberScreen(AccountDetail(account.id.value))

    AuthorizedAccountHeader(
        account.avatar,
        account.displayName,
        account.screen,
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
private fun ColumnScope.DrawerAccountList(
    others: List<Account>,
    drawerState: DrawerState,
    onClickOtherAccount: (Account) -> Unit,
) {
    val scope = rememberCoroutineScope()

    Box(Modifier.weight(1F)) {
        AuthorizedAccountList(
            others,
            onClick = {
                scope.handleOnClick(drawerState) {
                    onClickOtherAccount(it)
                }
            },
        )
    }
}

@Composable
private fun DrawerMenus(
    drawerState: DrawerState,
) {
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current
    val signIn = rememberSignInScreen()

    DrawerBottomMenus(
        onClickDrawerMenu = { item ->
            scope.handleOnClick(drawerState) {
                when (item) {
                    is DrawerBottomMenu.NewAccount -> navigator?.push(signIn)
                    is DrawerBottomMenu.Settings -> Unit
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
