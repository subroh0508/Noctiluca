package noctiluca.features.timeline.template.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.list.TwoLineListItem
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.features.timeline.state.CurrentAuthorizedAccount

@Composable
internal fun AuthorizedAccountsList(
    account: CurrentAuthorizedAccount,
    onClick: (Account) -> Unit,
) = LazyColumn(
    account.others,
    key = { it.screen },
) { _, item ->
    AuthorizedAccountItem(item) { onClick(item) }
}

@Composable
private fun AuthorizedAccountItem(
    account: Account,
    onClick: () -> Unit,
) = TwoLineListItem(
    account.displayName,
    supportingText = account.screen,
    leadingContent = {
        AsyncImage(
            account.avatar,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        )
    },
    modifier = Modifier.clickable { onClick() }
        .padding(horizontal = 12.dp)
)
