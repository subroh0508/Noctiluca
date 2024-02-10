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
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.model.accountdetail.AccountAttributes

@Composable
internal fun ActionMenu(
    username: String?,
    condition: AccountAttributes.Condition?,
    relationshipsModel: RelationshipsModel,
    openAddList: () -> Unit,
    openBrowser: () -> Unit,
    mute: () -> Unit,
    block: () -> Unit,
    report: () -> Unit,
    toggleReblogs: () -> Unit,
) {
    if (username == null || condition == AccountAttributes.Condition.SUSPENDED || relationshipsModel.relationships.me) {
        return
    }

    var expanded by remember { mutableStateOf(false) }
    val (
        openMuteDialog,
        openBlockDialog,
        openReportDialog,
    ) = Dialogs(
        username,
        mute = mute,
        block = block,
        report = report,
    )

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
                relationshipsModel.relationships,
                onDismissRequest = { expanded = false },
                onClick = openAddList,
            )
            FollowingAccountMenu(
                username,
                relationshipsModel.relationships,
                onDismissRequest = { expanded = false },
                toggleReblogs = toggleReblogs,
            )
            CommonMenu(
                username,
                relationshipsModel.relationships,
                onDismissRequest = { expanded = false },
                openMuteDialog,
                openBlockDialog,
                openReportDialog,
                unmute = mute,
            )
            Divider(Modifier.fillMaxWidth())
            OpenBrowser(onClick = openBrowser)
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
    mute: () -> Unit,
    block: () -> Unit,
    report: () -> Unit,
): List<MutableState<Boolean>> {
    val openMuteDialog = remember { mutableStateOf(false) }
    val openBlockDialog = remember { mutableStateOf(false) }
    val openReportDialog = remember { mutableStateOf(false) }

    MuteDialog(
        username,
        openMuteDialog,
        mute = mute,
    )

    BlockDialog(
        username,
        openBlockDialog,
        block = block,
    )

    ReportFullScreenDialog(
        username,
        openReportDialog,
        report = report,
    )

    return listOf(openMuteDialog, openBlockDialog, openReportDialog)
}
