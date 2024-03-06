package noctiluca.features.timeline.component.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.account.AccountName
import noctiluca.features.shared.atoms.appbar.NavigateIconSize
import noctiluca.features.shared.atoms.clickable
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.model.Uri

@Composable
internal fun AuthorizedAccountHeader(
    avatar: Uri?,
    displayName: String,
    screen: String,
    onClickOpenAccountDetail: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    Row {
        IconButton(onClick = onClickOpenAccountDetail) {
            AsyncImage(
                avatar,
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
        IconButton(onClick = {}) {
            Icon(
                Icons.Filled.Share,
                contentDescription = "Share",
            )
        }
    }
    Spacer(Modifier.height(16.dp))
    AccountName(
        displayName = displayName,
        screen = screen,
        modifier = Modifier.fillMaxWidth()
            .clickable(noRipple = true) { onClickOpenAccountDetail() },
    )
}
