package noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.getCommonString
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun Mute(
    username: String,
    relationships: Relationships,
    openDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
) {
    if (relationships.muting) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_mute.format(username)) },
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.VolumeOff, contentDescription = "Mute") },
        onClick = {
            onDismissRequest()
            openDialog.value = true
        },
    )
}

@Composable
internal fun MuteDialog(
    username: String,
    openDialog: MutableState<Boolean>,
    mute: () -> Unit,
) {
    if (!openDialog.value) {
        return
    }

    AlertDialog(
        text = { Text(getString().account_detail_menu_mute_description.format(username)) },
        confirmButton = {
            TextButton(
                onClick = {
                    mute()
                    openDialog.value = false
                },
            ) {
                Text(getString().account_detail_menu_mute_confirm)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                },
            ) {
                Text(getCommonString().cancel)
            }
        },
        onDismissRequest = { openDialog.value = false },
    )
}
