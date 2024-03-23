package noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.utils.format
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun Unmute(
    username: String,
    relationships: Relationships,
    onDismissRequest: () -> Unit,
    unmute: () -> Unit,
) {
    if (!relationships.muting) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_unmute.format(username)) },
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Unmute") },
        onClick = {
            onDismissRequest()
            unmute()
        },
    )
}
