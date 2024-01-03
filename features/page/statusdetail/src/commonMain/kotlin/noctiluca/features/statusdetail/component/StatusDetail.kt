package noctiluca.features.statusdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noctiluca.features.shared.account.TooterName
import noctiluca.features.shared.atoms.divider.Divider
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.features.shared.atoms.image.imageResources
import noctiluca.features.shared.atoms.text.HtmlText
import noctiluca.features.shared.getDrawables
import noctiluca.features.shared.status.Action
import noctiluca.features.shared.status.VisibilityIcon
import noctiluca.features.shared.utils.border
import noctiluca.features.shared.utils.format
import noctiluca.features.shared.utils.toDp
import noctiluca.features.shared.utils.toYearMonthDayTime
import noctiluca.features.statusdetail.getString
import noctiluca.model.account.Account
import noctiluca.model.status.Status

@Composable
internal fun StatusDetail(
    status: Status,
    onClickAction: (Action) -> Unit,
) = Column(
    modifier = Modifier.border(
        top = Dp.Hairline,
        bottom = Dp.Hairline,
    ).padding(
        start = 16.dp,
        end = 16.dp,
        top = 16.dp,
    ),
) {
    StatusDetailHeader(
        status.tooter,
        status.visibility,
    )

    Spacer(Modifier.height(8.dp))

    HtmlText(
        status.content,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 20.sp,
            lineHeight = 28.sp,
        ),
    )

    CreatedAtAndVia(status)
    Divider(Modifier.fillMaxWidth())
    StatusDetailCount(status)
    StatusDetailActions(
        status,
        onClickAction = onClickAction,
    )
}

@Composable
private fun StatusDetailHeader(
    tooter: Account,
    visibility: Status.Visibility,
) = Row {
    var tooterIconSize by remember { mutableStateOf(0) }

    AsyncImage(
        tooter.avatar,
        fallback = imageResources(getDrawables().icon_mastodon),
        modifier = Modifier.size(tooterIconSize.toDp())
            .clip(RoundedCornerShape(8.dp)),
    )

    Spacer(Modifier.width(16.dp))

    TooterName(
        tooter,
        modifier = Modifier.weight(1F, true)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                tooterIconSize = placeable.height

                layout(placeable.width, placeable.height) { placeable.place(0, 0) }
            },
        trailing = {
            VisibilityIcon(
                visibility,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(MaterialTheme.typography.titleMedium.fontSize.toDp()),
            )
        },
    )
}

@Composable
private fun CreatedAtAndVia(
    status: Status,
) = CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.outline,
    LocalTextStyle provides MaterialTheme.typography.titleSmall,
) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(status.createdAt.toYearMonthDayTime())
        Spacer(Modifier.width(8.dp))

        val via = status.via
        if (via != null) {
            Text("･")
            Spacer(Modifier.width(8.dp))
            Text("via ${via.name}")
        }
    }
}

@Composable
private fun StatusDetailCount(
    status: Status,
) {
    if (status.reblogCount == 0 && status.favouriteCount == 0) {
        return
    }

    Column(Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            if (status.reblogCount != 0) {
                Text(
                    format(status.reblogCount),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.alignByBaseline(),
                )
                Text(
                    getString().status_detail_boost,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alignByBaseline(),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            if (status.favouriteCount != 0) {
                Text(
                    format(status.favouriteCount),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.alignByBaseline(),
                )
                Text(
                    getString().status_detail_favourite,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}

@Suppress("LongMethod")
@Composable
private fun StatusDetailActions(
    status: Status,
    onClickAction: (Action) -> Unit,
) = CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.outline,
) {
    Row {
        IconButton(
            onClick = { onClickAction(Action.REPLY) },
            modifier = Modifier.height(48.dp)
                .weight(1F)
        ) {
            Icon(
                Icons.Default.Reply,
                contentDescription = "reply",
            )
        }

        IconButton(
            onClick = { onClickAction(Action.BOOST) },
            modifier = Modifier.height(48.dp)
                .weight(1F)
        ) {
            Icon(
                Icons.Default.Repeat,
                contentDescription = "boost",
                tint = if (status.reblogged) Color.Green else LocalContentColor.current,
            )
        }

        IconButton(
            onClick = { onClickAction(Action.FAVOURITE) },
            modifier = Modifier.height(48.dp)
                .weight(1F)
        ) {
            Icon(
                if (status.favourited) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "favourite",
                tint = if (status.favourited) Color.Yellow else LocalContentColor.current,
            )
        }

        IconButton(
            onClick = { /* onClickAction(Action.BOOKMARK) */ },
            modifier = Modifier.height(48.dp)
                .weight(1F)
        ) {
            Icon(
                if (status.bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "bookmark",
                tint = if (status.bookmarked) Color.Blue else LocalContentColor.current,
            )
        }

        IconButton(
            onClick = { onClickAction(Action.SHARE) },
            modifier = Modifier.height(48.dp)
                .weight(1F)
        ) {
            Icon(
                Icons.Default.Share,
                contentDescription = "share",
            )
        }

        IconButton(
            onClick = { onClickAction(Action.OTHERS) },
            modifier = Modifier.height(48.dp)
                .weight(1F)
        ) {
            Icon(
                Icons.Default.MoreHoriz,
                contentDescription = "others",
            )
        }
    }
}
