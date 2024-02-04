package noctiluca.features.accountdetail.component.topappbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.*
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.AddListMenu
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.BlockDialog
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.MuteDialog
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.ReportFullScreenDialog
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.model.accountdetail.AccountAttributes

@Composable
internal fun ActionMenu(
    username: String?,
    condition: AccountAttributes.Condition?,
    viewModel: AccountRelationshipsViewModel,
) {
    val uiModel by viewModel.uiModel.collectAsState()

    if (username == null || condition == AccountAttributes.Condition.SUSPENDED || uiModel.relationships.me) {
        return
    }

    var expanded by remember { mutableStateOf(false) }
    val (
        openMuteDialog,
        openBlockDialog,
        openReportDialog,
    ) = Dialogs(username, viewModel)

    IconButton(
        onClick = {}
    ) {
        Icon(Icons.Default.Share, contentDescription = "Share Account")
    }

    Box {
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Open Action Menu")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            AddListMenu(
                uiModel.relationships,
                onDismissRequest = { expanded = false },
                onClick = {},
            )
            FollowingAccountMenu(
                username,
                uiModel.relationships,
                onDismissRequest = { expanded = false },
                toggleReblogs = { viewModel.toggleReblogs() },
            )
            CommonMenu(
                username,
                uiModel.relationships,
                onDismissRequest = { expanded = false },
                openMuteDialog,
                openBlockDialog,
                openReportDialog,
                unmute = { viewModel.mute() },
            )
            Divider(Modifier.fillMaxWidth())
            OpenBrowser(onClick = {})
        }
    }
}

@Composable
private fun OpenBrowser(
    onClick: () -> Unit,
) = DropdownMenuItem(
    text = { Text(getString().account_detail_menu_open_browser) },
    leadingIcon = { Icon(Icons.Default.OpenInBrowser, contentDescription = "Open Browser") },
    onClick = onClick,
)

@Composable
private fun Dialogs(
    username: String,
    viewModel: AccountRelationshipsViewModel,
): List<MutableState<Boolean>> {
    val openMuteDialog = remember { mutableStateOf(false) }
    val openBlockDialog = remember { mutableStateOf(false) }
    val openReportDialog = remember { mutableStateOf(false) }

    MuteDialog(
        username,
        openMuteDialog,
        mute = { viewModel.mute() },
    )

    BlockDialog(
        username,
        openBlockDialog,
        block = { viewModel.block() },
    )

    ReportFullScreenDialog(
        username,
        openReportDialog,
        report = {},
    )

    return listOf(openMuteDialog, openBlockDialog, openReportDialog)
}
