package noctiluca.features.timeline.template.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.features.timeline.model.CurrentAuthorizedAccount
import noctiluca.features.timeline.template.drawer.header.CurrentAuthorizedAccountHeader
import noctiluca.features.timeline.template.drawer.menu.AuthorizedAccountsList
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenus
import noctiluca.model.account.Account

@Composable
internal fun TimelineNavigationDrawer(
    account: CurrentAuthorizedAccount,
    onClickTopAccount: (Account) -> Unit,
    onClickOtherAccount: (Account) -> Unit,
    onClickDrawerMenu: (TimelineDrawerMenu) -> Unit,
    content: @Composable (DrawerState) -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            TimelineDrawerSheet(
                account,
                drawerState,
                onClickTopAccount,
                onClickOtherAccount,
                onClickDrawerMenu,
            )
        },
        drawerState = drawerState,
    ) { content(drawerState) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineDrawerSheet(
    account: CurrentAuthorizedAccount,
    drawerState: DrawerState,
    onClickTopAccount: (Account) -> Unit,
    onClickOtherAccount: (Account) -> Unit,
    onClickDrawerMenu: (TimelineDrawerMenu) -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        Modifier.fillMaxHeight(),
    ) {
        Spacer(Modifier.height(12.dp))
        account.current?.let {
            CurrentAuthorizedAccountHeader(
                it,
                onClickOpenAccountDetail = {
                    scope.handleOnClick(drawerState) {
                        onClickTopAccount(it)
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        horizontal = 28.dp,
                        vertical = 16.dp,
                    ),
            )
        }

        HorizontalDivider()

        Box(Modifier.weight(1F)) {
            AuthorizedAccountsList(
                account,
                onClick = {
                    scope.handleOnClick(drawerState) {
                        onClickOtherAccount(it)
                    }
                },
            )
        }

        HorizontalDivider()

        TimelineDrawerMenus(
            onClickDrawerMenu = {
                scope.handleOnClick(drawerState) {
                    onClickDrawerMenu(it)
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun CoroutineScope.handleOnClick(
    drawerState: DrawerState,
    onClick: () -> Unit,
) = launch {
    drawerState.close()
    onClick()
}
