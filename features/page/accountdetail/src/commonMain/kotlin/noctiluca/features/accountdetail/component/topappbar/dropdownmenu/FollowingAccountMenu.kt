package noctiluca.features.accountdetail.component.topappbar.dropdownmenu

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.HideReblogs
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.ShowReblogs
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun FollowingAccountMenu(
    username: String,
    relationships: Relationships,
    onDismissRequest: () -> Unit,
    toggleReblogs: () -> Unit,
) {
    if (!relationships.following) {
        return
    }

    HideReblogs(
        username,
        relationships,
        onDismissRequest = onDismissRequest,
        hideReblogs = toggleReblogs,
    )
    ShowReblogs(
        username,
        relationships,
        onDismissRequest = onDismissRequest,
        showReblogs = toggleReblogs,
    )
    HorizontalDivider()
}
