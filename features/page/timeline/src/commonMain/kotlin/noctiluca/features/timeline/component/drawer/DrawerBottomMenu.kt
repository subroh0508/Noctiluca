package noctiluca.features.timeline.component.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import noctiluca.features.timeline.getString

internal sealed class DrawerBottomMenu {
    abstract val label: String
    abstract val icon: Pair<ImageVector, String>

    data class NewAccount(
        override val label: String,
        override val icon: Pair<ImageVector, String> = Icons.Default.AddCircle to "NewAccount",
    ) : DrawerBottomMenu()

    data class Settings(
        override val label: String,
        override val icon: Pair<ImageVector, String> = Icons.Default.Settings to "Settings",
    ) : DrawerBottomMenu()

    companion object {
        @Composable
        fun Build() = listOf(
            NewAccount(getString().timeline_new_account),
            Settings(getString().timeline_settings),
        )
    }
}

@Composable
internal fun DrawerBottomMenus(
    onClickDrawerMenu: (DrawerBottomMenu) -> Unit,
) = DrawerBottomMenu.Build().forEach { item ->
    DrawerMenuItem(
        item.icon,
        item.label,
        onClick = { onClickDrawerMenu(item) },
    )
}

@Composable
private fun DrawerMenuItem(
    icon: Pair<ImageVector, String>,
    label: String,
    onClick: () -> Unit,
) = ListItem(
    { Text(label) },
    leadingContent = {
        Icon(icon.first, contentDescription = icon.second)
    },
    modifier = Modifier.clickable { onClick() }
        .padding(horizontal = 12.dp)
)
