package noctiluca.features.accountdetail.component.topappbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.getString
import noctiluca.model.accountdetail.AccountAttributes

@Composable
internal fun ActionMenu(
    detail: AccountAttributes?,
) {
    detail ?: return

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
            if (detail.relationships.following) {
                Divider(Modifier.fillMaxWidth())
                HideReblogs(detail, onClick = {})
                ShowReblogs(detail, onClick = {})
            }
            Divider(Modifier.fillMaxWidth())
            Mute(detail, onClick = {})
            Unmute(detail, onClick = {})
            Block(detail, onClick = {})
            Report(detail, onClick = {})
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
    detail: AccountAttributes,
    onClick: () -> Unit,
) {
    if (detail.relationships.muting) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_mute.format(detail.username)) },
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.VolumeMute, contentDescription = "Mute") },
        onClick = onClick,
    )
}

@Composable
private fun Unmute(
    detail: AccountAttributes,
    onClick: () -> Unit,
) {
    if (!detail.relationships.muting) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_unmute.format(detail.username)) },
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Unmute") },
        onClick = onClick,
    )
}

@Composable
private fun Block(
    detail: AccountAttributes,
    onClick: () -> Unit,
) {
    if (detail.relationships.blocking) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_block.format(detail.username)) },
        leadingIcon = { Icon(Icons.Default.Block, contentDescription = "Block") },
        onClick = onClick,
    )
}

@Composable
private fun Report(
    detail: AccountAttributes,
    onClick: () -> Unit,
) {
    if (detail.relationships.blocking) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_report.format(detail.username)) },
        leadingIcon = { Icon(Icons.Default.Report, contentDescription = "Report") },
        onClick = onClick,
    )
}

@Composable
private fun HideReblogs(
    detail: AccountAttributes,
    onClick: () -> Unit,
) {
    if (!detail.relationships.showReblogs) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_hide_reblogs.format(detail.username)) },
        leadingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = "Hide Reblogs") },
        onClick = onClick,
    )
}

@Composable
private fun ShowReblogs(
    detail: AccountAttributes,
    onClick: () -> Unit,
) {
    if (detail.relationships.showReblogs) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_show_reblogs.format(detail.username)) },
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
