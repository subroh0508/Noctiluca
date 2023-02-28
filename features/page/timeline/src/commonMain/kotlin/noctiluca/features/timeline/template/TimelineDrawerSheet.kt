package noctiluca.features.timeline.template

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.account.AccountHeader
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.state.CurrentAuthorizedAccount

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineDrawerSheet(
    account: CurrentAuthorizedAccount,
    onClose: () -> Unit,
) = ModalDrawerSheet(
    Modifier.fillMaxHeight(),
) {
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

    Divider(Modifier.fillMaxWidth())

    Box(Modifier.weight(1F))

    Divider(Modifier.fillMaxWidth())

    TimelineDrawerMenu.Build().forEach { item ->
        TimelineDrawerMenuItem(
            item.icon,
            item.label,
            onClick = { onClose() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineDrawerMenuItem(
    icon: Pair<ImageVector, String>,
    label: String,
    onClick: () -> Unit,
    colors: NavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(),
) = Surface(
    onClick = onClick,
    modifier = Modifier.height(56.dp)
        .fillMaxWidth(),
) {
    Row(
        Modifier.padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconColor = colors.iconColor(selected = false).value
        val labelColor = colors.textColor(selected = false).value

        CompositionLocalProvider(
            LocalContentColor provides iconColor,
        ) {
            Icon(icon.first, contentDescription = icon.second)
        }

        Spacer(Modifier.width(12.dp))

        CompositionLocalProvider(
            LocalContentColor provides labelColor,
        ) {
            Text(label)
        }
    }
}
