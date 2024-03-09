package noctiluca.features.statusdetail.component.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.status.Action

@Composable
internal fun Actions(
    reblogged: Boolean,
    favourited: Boolean,
    bookmarked: Boolean,
    onClickAction: (Action) -> Unit,
) = CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.outline,
) {
    Row {
        ReplyButton(onClickAction)
        BoostButton(reblogged, onClickAction)
        FavouriteButton(favourited, onClickAction)
        BookmarkButton(bookmarked, onClickAction)
        ShareButton(onClickAction)
        OthersButton(onClickAction)
    }
}

@Composable
private fun RowScope.ReplyButton(
    onClickAction: (Action) -> Unit,
) = IconButton(
    onClick = { onClickAction(Action.REPLY) },
    modifier = Modifier.height(48.dp)
        .weight(1F)
) {
    Icon(
        Icons.AutoMirrored.Filled.Reply,
        contentDescription = "reply",
    )
}

@Composable
private fun RowScope.BoostButton(
    reblogged: Boolean,
    onClickAction: (Action) -> Unit,
) = IconButton(
    onClick = { onClickAction(Action.BOOST) },
    modifier = Modifier.height(48.dp)
        .weight(1F)
) {
    Icon(
        Icons.Default.Repeat,
        contentDescription = "boost",
        tint = if (reblogged) Color.Green else LocalContentColor.current,
    )
}

@Composable
private fun RowScope.FavouriteButton(
    favourited: Boolean,
    onClickAction: (Action) -> Unit,
) = IconButton(
    onClick = { onClickAction(Action.FAVOURITE) },
    modifier = Modifier.height(48.dp)
        .weight(1F)
) {
    Icon(
        if (favourited) Icons.Default.Star else Icons.Default.StarBorder,
        contentDescription = "favourite",
        tint = if (favourited) Color.Yellow else LocalContentColor.current,
    )
}

@Composable
private fun RowScope.BookmarkButton(
    bookmarked: Boolean,
    onClickAction: (Action) -> Unit,
) = IconButton(
    onClick = { /* onClickAction(Action.BOOKMARK) */ },
    modifier = Modifier.height(48.dp)
        .weight(1F)
) {
    Icon(
        if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
        contentDescription = "bookmark",
        tint = if (bookmarked) Color.Blue else LocalContentColor.current,
    )
}

@Composable
private fun RowScope.ShareButton(
    onClickAction: (Action) -> Unit,
) = IconButton(
    onClick = { onClickAction(Action.SHARE) },
    modifier = Modifier.height(48.dp)
        .weight(1F)
) {
    Icon(
        Icons.Default.Share,
        contentDescription = "share",
    )
}

@Composable
private fun RowScope.OthersButton(
    onClickAction: (Action) -> Unit,
) = IconButton(
    onClick = { onClickAction(Action.OTHERS) },
    modifier = Modifier.height(48.dp)
        .weight(1F)
) {
    Icon(
        Icons.Default.MoreHoriz,
        contentDescription = "others",
    )
}
