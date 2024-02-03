package noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.getCommonString
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun Block(
    username: String,
    relationships: Relationships,
    openDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
) {
    if (relationships.blocking) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_block.format(username)) },
        leadingIcon = { Icon(Icons.Default.Block, contentDescription = "Block") },
        onClick = {
            onDismissRequest()
            openDialog.value = true
        },
    )
}

@Composable
internal fun BlockDialog(
    username: String,
    openDialog: MutableState<Boolean>,
    block: () -> Unit,
) {
    if (!openDialog.value) {
        return
    }

    AlertDialog(
        text = { Text(getString().account_detail_menu_block_description.format(username)) },
        confirmButton = {
            TextButton(
                onClick = {
                    block()
                    openDialog.value = false
                },
            ) {
                Text(getString().account_detail_menu_block_confirm)
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
