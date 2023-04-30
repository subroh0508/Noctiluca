package noctiluca.features.timeline.template.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account
import noctiluca.features.components.atoms.appbar.NavigateIconSize
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.shared.account.AccountName

@Composable
internal fun CurrentAuthorizedAccountHeader(
    account: Account,
    onClickOpenAccountDetail: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    Row {
        IconButton(onClick = onClickOpenAccountDetail) {
            AsyncImage(
                account.avatar,
                // fallback = imageResources(getDrawables().icon_mastodon),
                modifier = Modifier.size(NavigateIconSize)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        Spacer(Modifier.weight(1F))

        IconButton(onClick = {}) {
            Icon(
                Icons.Default.BookmarkBorder,
                contentDescription = "Bookmark",
            )
        }
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
            )
        }
    }
    Spacer(Modifier.height(16.dp))
    AccountName(
        account,
        modifier = Modifier.clickable { onClickOpenAccountDetail() },
    )
}
