package noctiluca.features.accountdetail.component.topappbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun ActionMenu(
    username: String?,
    viewModel: AccountRelationshipsViewModel,
) {
    val uiModel by viewModel.uiModel.collectAsState()

    if (username == null || uiModel.relationships.me) {
        return
    }

    var expanded by remember { mutableStateOf(false) }

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
            AddList(onClick = {})
            if (uiModel.relationships.following) {
                Divider(Modifier.fillMaxWidth())
                HideReblogs(username, uiModel.relationships, onClick = {})
                ShowReblogs(username, uiModel.relationships, onClick = {})
            }
            Divider(Modifier.fillMaxWidth())
            Mute(username, uiModel.relationships, onClick = { viewModel.mute() })
            Unmute(username, uiModel.relationships, onClick = { viewModel.mute() })
            Block(username, uiModel.relationships, onClick = { viewModel.block() })
            Report(username, uiModel.relationships, onClick = {})
            Divider(Modifier.fillMaxWidth())
            OpenBrowser(onClick = {})
        }
    }
}

@Composable
private fun AddList(
    onClick: () -> Unit,
) = DropdownMenuItem(
    text = { Text(getString().account_detail_menu_add_list) },
    leadingIcon = { Icon(Icons.AutoMirrored.Filled.PlaylistAdd, contentDescription = "Add List") },
    onClick = onClick,
)

@Composable
private fun Mute(
    username: String,
    relationships: Relationships,
    onClick: () -> Unit,
) {
    if (relationships.muting) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_mute.format(username)) },
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.VolumeOff, contentDescription = "Mute") },
        onClick = onClick,
    )
}

@Composable
private fun Unmute(
    username: String,
    relationships: Relationships,
    onClick: () -> Unit,
) {
    if (!relationships.muting) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_unmute.format(username)) },
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Unmute") },
        onClick = onClick,
    )
}

@Composable
private fun Block(
    username: String,
    relationships: Relationships,
    onClick: () -> Unit,
) {
    if (relationships.blocking) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_block.format(username)) },
        leadingIcon = { Icon(Icons.Default.Block, contentDescription = "Block") },
        onClick = onClick,
    )
}

@Composable
private fun Report(
    username: String,
    relationships: Relationships,
    onClick: () -> Unit,
) {
    if (relationships.blocking) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_report.format(username)) },
        leadingIcon = { Icon(Icons.Default.Report, contentDescription = "Report") },
        onClick = onClick,
    )
}

@Composable
private fun HideReblogs(
    username: String,
    relationships: Relationships,
    onClick: () -> Unit,
) {
    if (!relationships.showReblogs) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_hide_reblogs.format(username)) },
        leadingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = "Hide Reblogs") },
        onClick = onClick,
    )
}

@Composable
private fun ShowReblogs(
    username: String,
    relationships: Relationships,
    onClick: () -> Unit,
) {
    if (relationships.showReblogs) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_show_reblogs.format(username)) },
        leadingIcon = { Icon(Icons.Default.Visibility, contentDescription = "Show Reblogs") },
        onClick = onClick,
    )
}

@Composable
private fun OpenBrowser(
    onClick: () -> Unit,
) = DropdownMenuItem(
    text = { Text(getString().account_detail_menu_open_browser) },
    leadingIcon = { Icon(Icons.Default.OpenInBrowser, contentDescription = "Open Browser") },
    onClick = onClick,
)
