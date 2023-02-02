package noctiluca.features.shared.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account
import noctiluca.features.components.atoms.appbar.NavigateIconSize
import noctiluca.features.components.atoms.image.AsyncImage

@Composable
fun AccountHeader(
    account: Account,
    onClickAccountIcon: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    IconButton(onClick = onClickAccountIcon) {
        AsyncImage(
            account.avatar,
            //fallback = imageResources(getDrawables().icon_mastodon),
            modifier = Modifier.size(NavigateIconSize)
                .clip(RoundedCornerShape(8.dp)),
        )
    }
    Spacer(Modifier.height(16.dp))
    AccountName(account)
}
