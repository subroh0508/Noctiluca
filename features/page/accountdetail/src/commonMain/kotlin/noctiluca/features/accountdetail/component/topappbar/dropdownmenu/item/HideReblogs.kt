package noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noctiluca.features.accountdetail.getString
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun HideReblogs(
    username: String,
    relationships: Relationships,
    onDismissRequest: () -> Unit,
    hideReblogs: () -> Unit,
) {
    if (!relationships.showReblogs) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_hide_reblogs.format(username)) },
        leadingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = "Hide Reblogs") },
        onClick = {
            onDismissRequest()
            hideReblogs()
        },
    )
}
