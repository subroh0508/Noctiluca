package noctiluca.features.timeline.template.drawer.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.list.OneLineListItem
import noctiluca.features.timeline.getString

internal sealed class TimelineDrawerMenu {
    abstract val label: String
    abstract val icon: Pair<ImageVector, String>

    data class NewAccount(
        override val label: String,
        override val icon: Pair<ImageVector, String> = Icons.Default.AddCircle to "NewAccount",
    ) : TimelineDrawerMenu()

    data class Settings(
        override val label: String,
        override val icon: Pair<ImageVector, String> = Icons.Default.Settings to "Settings",
    ) : TimelineDrawerMenu()

    companion object {
        @Composable
        fun Build() = listOf(
            NewAccount(getString().timeline_new_account),
            Settings(getString().timeline_settings),
        )
    }
}

@Composable
internal fun TimelineDrawerMenus(
    onClickDrawerMenu: (TimelineDrawerMenu) -> Unit,
) = TimelineDrawerMenu.Build().forEach { item ->
    TimelineDrawerMenuItem(
        item.icon,
        item.label,
        onClick = { onClickDrawerMenu(item) },
    )
}

@Composable
private fun TimelineDrawerMenuItem(
    icon: Pair<ImageVector, String>,
    label: String,
    onClick: () -> Unit,
) = OneLineListItem(
    label,
    Modifier.clickable { onClick() }
        .padding(horizontal = 12.dp)
) {
    Icon(icon.first, contentDescription = icon.second)
}
