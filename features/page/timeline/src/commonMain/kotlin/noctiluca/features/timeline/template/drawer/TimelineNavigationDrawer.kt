package noctiluca.features.timeline.template.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.account.model.Account
import noctiluca.features.timeline.state.CurrentAuthorizedAccountState
import noctiluca.features.timeline.template.drawer.header.CurrentAuthorizedAccountHeader
import noctiluca.features.timeline.template.drawer.menu.AuthorizedAccountsList
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineNavigationDrawer(
    authorizedAccountState: CurrentAuthorizedAccountState,
    onClickTopAccount: (Account) -> Unit,
    onClickDrawerMenu: (TimelineDrawerMenu) -> Unit,
    content: @Composable (DrawerState) -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            TimelineDrawerSheet(
                authorizedAccountState,
                drawerState,
                onClickTopAccount,
                onClickDrawerMenu,
            )
        },
        drawerState = drawerState,
    ) { content(drawerState) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineDrawerSheet(
    authorizedAccountState: CurrentAuthorizedAccountState,
    drawerState: DrawerState,
    onClickTopAccount: (Account) -> Unit,
    onClickDrawerMenu: (TimelineDrawerMenu) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val account = authorizedAccountState.value

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

        Divider(Modifier.fillMaxWidth())

        Box(Modifier.weight(1F)) {
            AuthorizedAccountsList(
                account,
                onClick = {
                    scope.handleOnClick(drawerState) {
                        authorizedAccountState.switch(scope, it)
                    }
                },
            )
        }

        Divider(Modifier.fillMaxWidth())

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
