package noctiluca.features.timeline.screen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.timeline.TimelinesScreen
import noctiluca.features.timeline.section.TimelinesDrawerSheet
import noctiluca.features.timeline.viewmodel.AuthorizedAccountViewModel
import noctiluca.model.Domain
import noctiluca.model.account.Account

@Composable
internal fun TimelinesScreen.TimelinesNavigationDrawer(
    content: @Composable (Account?, Domain?, DrawerState) -> Unit,
) {
    val viewModel: AuthorizedAccountViewModel = getAuthorizedScreenModel()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val uiModel by viewModel.uiModel.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {
            TimelinesDrawerSheet(
                uiModel,
                drawerState,
                onClickOtherAccount = { viewModel.switch(it) },
            )
        },
        drawerState = drawerState,
    ) { content(uiModel.current, uiModel.domain, drawerState) }
}
