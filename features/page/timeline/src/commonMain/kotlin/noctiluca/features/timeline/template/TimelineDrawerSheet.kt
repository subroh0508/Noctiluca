package noctiluca.features.timeline.template

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.list.OneLineListItem
import noctiluca.features.components.atoms.list.TwoLineListItem
import noctiluca.features.components.molecules.list.LazyColumn
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
    onClick: (TimelineDrawerMenu) -> Unit,
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

    Box(Modifier.weight(1F)) {
        AuthorizedAccountsList(account)
    }

    Divider(Modifier.fillMaxWidth())

    TimelineDrawerMenu.Build().forEach { item ->
        TimelineDrawerMenuItem(
            item.icon,
            item.label,
            onClick = { onClick(item) },
        )
    }
}

@Composable
private fun AuthorizedAccountsList(
    account: CurrentAuthorizedAccount,
) = LazyColumn(
    account.all,
    key = { it.screen },
) { _, item ->
    AuthorizedAccountItem(item)
}

@Composable
private fun AuthorizedAccountItem(
    account: Account,
) = TwoLineListItem(
    account.displayName,
    supportingText = account.screen,
    leadingContent = {
        AsyncImage(
            account.avatar,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        )
    }
)

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
