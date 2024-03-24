package noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.utils.format
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun ShowReblogs(
    username: String,
    relationships: Relationships,
    onDismissRequest: () -> Unit,
    showReblogs: () -> Unit,
) {
    if (relationships.showReblogs) {
        return
    }

    DropdownMenuItem(
        text = { Text(getString().account_detail_menu_show_reblogs.format(username)) },
        leadingIcon = { Icon(Icons.Default.Visibility, contentDescription = "Show Reblogs") },
        onClick = {
            onDismissRequest()
            showReblogs()
        },
    )
}
