package noctiluca.features.accountdetail.component.topappbar.dropdownmenu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.HideReblogs
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.ShowReblogs
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun FollowingAccountMenu(
    username: String,
    relationships: Relationships,
    onDismissRequest: () -> Unit,
    hideReblogs: () -> Unit,
    showReblogs: () -> Unit,
) {
    if (!relationships.following) {
        return
    }

    HideReblogs(
        username,
        relationships,
        onDismissRequest = onDismissRequest,
        hideReblogs = hideReblogs,
    )
    ShowReblogs(
        username,
        relationships,
        onDismissRequest = onDismissRequest,
        showReblogs = showReblogs,
    )
    Divider(Modifier.fillMaxWidth())
}
