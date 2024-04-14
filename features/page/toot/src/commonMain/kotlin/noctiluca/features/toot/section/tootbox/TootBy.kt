package noctiluca.features.toot.section.tootbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.account.AccountName
import noctiluca.features.shared.components.appbar.NavigateIconSize
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.model.account.Account

@Composable
internal fun TootBy(
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
