package noctiluca.features.shared.toot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account
import noctiluca.features.components.atoms.appbar.NavigateIconSize
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.shared.account.AccountName
import noctiluca.features.shared.toot.internal.TootAreaPadding
import noctiluca.features.shared.toot.internal.TootTextArea
import noctiluca.features.shared.toot.internal.VisibilityChip
import noctiluca.status.model.Status

@Composable
fun TootBox(
    account: Account?,
    content: MutableState<String?>,
    warning: MutableState<String?>,
    visibility: MutableState<Status.Visibility>,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    Row(
        modifier = Modifier.padding(horizontal = TootAreaPadding),
    ) {
        TootBy(
            account,
            modifier = Modifier.weight(1f),
        )
        VisibilityChip(visibility)
    }

    TootTextArea(
        content.value ?: "",
        warning.value ?: "",
        onChangeContent = { content.value = it },
        onChangeWarningText = { warning.value = it },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TootBy(
    account: Account?,
    modifier: Modifier = Modifier,
) {
    if (account == null) {
        Spacer(modifier)

        return
    }

    Row(modifier) {
        IconButton(onClick = {}) {
            AsyncImage(
                account.avatar,
                // fallback = imageResources(getDrawables().icon_mastodon),
                modifier = Modifier.size(NavigateIconSize)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        AccountName(account)
    }
}
