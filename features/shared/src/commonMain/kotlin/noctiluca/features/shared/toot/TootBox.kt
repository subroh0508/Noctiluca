package noctiluca.features.shared.toot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.account.AccountName
import noctiluca.features.shared.components.appbar.NavigateIconSize
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.shared.toot.internal.TootAreaPadding
import noctiluca.features.shared.toot.internal.TootTextArea
import noctiluca.model.account.Account

@Composable
fun TootBox(
    account: Account?,
    content: MutableState<String?>,
    warning: MutableState<String?>,
    onClickToot: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    TootBy(
        account,
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = TootAreaPadding),
    )

    TootTextArea(
        content.value ?: "",
        warning.value ?: "",
        onChangeContent = { content.value = it },
        onChangeWarningText = { warning.value = it },
        onClickToot = onClickToot,
        textAreaModifier = Modifier.weight(1F),
        modifier = Modifier.fillMaxSize(),
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
        Spacer(Modifier.width(8.dp))
        AccountName(
            account,
            displayNameStyle = MaterialTheme.typography.titleMedium,
            usernameStyle = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}
