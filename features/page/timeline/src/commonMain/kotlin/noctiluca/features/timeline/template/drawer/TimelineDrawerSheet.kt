package noctiluca.features.timeline.template.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account
import noctiluca.features.timeline.state.CurrentAuthorizedAccount
import noctiluca.features.timeline.template.drawer.menu.AuthorizedAccountsList
import noctiluca.features.timeline.template.drawer.header.CurrentAuthorizedAccountHeader
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineDrawerSheet(
    account: CurrentAuthorizedAccount,
    onClickTopAccount: (Account) -> Unit,
    onClickAccountList: (Account) -> Unit,
    onClickDrawerMenu: (TimelineDrawerMenu) -> Unit,
) = ModalDrawerSheet(
    Modifier.fillMaxHeight(),
) {
    Spacer(Modifier.height(12.dp))
    account.current?.let {
        CurrentAuthorizedAccountHeader(
            it,
            onClickOpenAccountDetail = { onClickTopAccount(it) },
            modifier = Modifier.fillMaxWidth()
                .padding(
                    horizontal = 28.dp,
                    vertical = 16.dp,
                ),
        )
    }

    Divider(Modifier.fillMaxWidth())

    Box(Modifier.weight(1F)) {
        AuthorizedAccountsList(account, onClickAccountList)
    }

    Divider(Modifier.fillMaxWidth())

    TimelineDrawerMenus(onClickDrawerMenu)
}
