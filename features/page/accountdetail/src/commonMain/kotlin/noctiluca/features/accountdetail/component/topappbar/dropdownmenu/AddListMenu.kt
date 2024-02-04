package noctiluca.features.accountdetail.component.topappbar.dropdownmenu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.getString
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun AddListMenu(
    relationships: Relationships,
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
) {
    if (relationships.blocking) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_add_list) },
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.PlaylistAdd, contentDescription = "Add List") },
        onClick = {
            onDismissRequest()
            onClick()
        },
    )
    Divider(Modifier.fillMaxWidth())
}
