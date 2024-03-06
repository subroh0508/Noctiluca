package noctiluca.features.shared.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.LocalDateTime
import noctiluca.features.shared.account.TooterName
import noctiluca.features.shared.atoms.clickable
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.features.shared.atoms.image.imageResources
import noctiluca.features.shared.atoms.text.HtmlText
import noctiluca.features.shared.atoms.text.RelativeTime
import noctiluca.features.shared.getDrawables
import noctiluca.features.shared.utils.baseline
import noctiluca.features.shared.utils.toDp
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.account.Account
import noctiluca.model.status.Status

enum class Action {
    REPLY, BOOST, FAVOURITE, SHARE, OTHERS
}

@Composable
fun Status(
    status: Status,
    onClick: ((StatusId) -> Unit)? = null,
    onClickAvatar: ((AccountId) -> Unit)? = null,
    onClickAction: ((Action) -> Unit)? = null,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier.run {
        if (onClick != null) {
            clickable { onClick(status.id) }
        } else {
            this
        }
    }.padding(
        start = 16.dp,
        end = 16.dp,
        top = 16.dp,
    ).then(modifier),
) {
    StatusHeader(
        status.tooter,
        status.visibility,
        status.createdAt,
        onClickAvatar,
    )

    Spacer(Modifier.height(8.dp))

    HtmlText(
        status.content,
        style = MaterialTheme.typography.bodyLarge,
    )

    if (onClickAction != null) {
        StatusActions(
            status,
            onClickAction,
        )
    }
}

@Composable
private fun StatusHeader(
    tooter: Account,
    visibility: Status.Visibility,
    createdAt: LocalDateTime,
    onClickAvatar: ((AccountId) -> Unit)?,
) = Row {
    var tooterIconSize by remember { mutableStateOf(0) }

    AsyncImage(
        tooter.avatar,
        fallback = imageResources(getDrawables().icon_mastodon),
        modifier = Modifier.size(tooterIconSize.toDp())
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClickAvatar?.invoke(tooter.id) },
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
        trailing = { baselineModifier ->
            VisibilityIcon(
                visibility,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(MaterialTheme.typography.titleSmall.fontSize.toDp())
                    .baseline(Alignment.Bottom, topOffset = 8)
                    .then(baselineModifier),
            )

            RelativeTime(
                createdAt,
                modifier = baselineModifier.padding(start = 2.dp),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleSmall,
            )
        },
    )
}

@Composable
fun VisibilityIcon(
    visibility: Status.Visibility,
    tint: Color = LocalContentColor.current,
    modifier: Modifier = Modifier,
) {
    val icon = when (visibility) {
        Status.Visibility.PUBLIC -> Icons.Default.Public
        Status.Visibility.UNLISTED -> Icons.Default.LockOpen
        Status.Visibility.PRIVATE -> Icons.Default.Lock
        Status.Visibility.DIRECT -> Icons.Default.AlternateEmail
    }

    Icon(
        icon,
        contentDescription = visibility.name,
        modifier,
        tint = tint,
    )
}

@Composable
private fun StatusActions(
    status: Status,
    onClick: (Action) -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.outline) {
        Row {
            ActionIcon(
                Icons.Default.Reply,
                status.repliesCount,
                contentDescription = "reply",
                onClick = { onClick(Action.REPLY) },
            )

            ActionIcon(
                Icons.Default.Repeat,
                status.reblogCount,
                contentDescription = "boost",
                onClick = { onClick(Action.BOOST) },
                tint = if (status.reblogged) Color.Green else LocalContentColor.current,
            )

            ActionIcon(
                if (status.favourited) Icons.Default.Star else Icons.Default.StarBorder,
                status.favouriteCount,
                contentDescription = "favourite",
                onClick = { onClick(Action.FAVOURITE) },
                tint = if (status.favourited) Color.Yellow else LocalContentColor.current,
            )

            ActionIcon(
                Icons.Default.Share,
                contentDescription = "share",
                onClick = { onClick(Action.SHARE) },
            )

            ActionIcon(
                Icons.Default.MoreHoriz,
                contentDescription = "others",
                onClick = { onClick(Action.OTHERS) },
            )
        }
    }
}

@Composable
private fun RowScope.ActionIcon(
    imageVector: ImageVector,
    count: Int? = null,
    contentDescription: String?,
    onClick: CoroutineScope.() -> Unit,
    tint: Color = LocalContentColor.current,
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier.height(48.dp)
            .weight(1F),
    ) {
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector,
            contentDescription,
            modifier = Modifier.size(20.dp)
                .align(Alignment.CenterVertically)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onClick(scope) },
            tint = tint,
        )
        Text(
            count?.takeIf { it > 0 }?.toString() ?: "",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 4.dp)
                .align(Alignment.CenterVertically),
        )
    }
}
