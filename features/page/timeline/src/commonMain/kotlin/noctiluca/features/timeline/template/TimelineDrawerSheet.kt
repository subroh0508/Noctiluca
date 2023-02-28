package noctiluca.features.timeline.template

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.account.AccountHeader
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.state.CurrentAuthorizedAccount

internal sealed class DrawerMenu {
    abstract val label: String
    abstract val icon: Pair<ImageVector, String>

    data class Profile(
        override val label: String,
        override val icon: Pair<ImageVector, String> = Icons.Default.Person to "Profile",
    ) : DrawerMenu()

    data class Settings(
        override val label: String,
        override val icon: Pair<ImageVector, String> = Icons.Default.Settings to "Settings",
    ) : DrawerMenu()

    companion object {
        @Composable
        fun Build() = listOf(
            Profile(getString().timeline_profile),
            Settings(getString().timeline_settings),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineDrawerSheet(
    account: CurrentAuthorizedAccount,
    onClose: () -> Unit,
) = ModalDrawerSheet {
    Spacer(Modifier.height(12.dp))
    account.current?.let {
        AccountHeader(
            it,
            onClickAccountIcon = {},
            modifier = Modifier.padding(
                horizontal = 28.dp,
                vertical = 16.dp,
            ),
        )
    }

    DrawerMenu.Build().forEach { item ->
        TimelineDrawerMenuItem(
            item.icon,
            item.label,
            selected = false,
            onClick = { onClose() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineDrawerMenuItem(
    icon: Pair<ImageVector, String>,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) = NavigationDrawerItem(
    icon = { Icon(icon.first, contentDescription = icon.second) },
    label = { Text(label) },
    selected = selected,
    onClick = onClick,
    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
)
