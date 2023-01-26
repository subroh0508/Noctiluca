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
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.atoms.text.RelativeTime
import noctiluca.features.components.utils.baseline
import noctiluca.features.components.utils.toDp
import noctiluca.features.shared.account.TooterName
import noctiluca.status.model.Status
import noctiluca.status.model.Tooter

enum class Action {
    REPLY, BOOST, FAVOURITE, SHARE, OTHERS
}

@Composable
fun Status(
    status: Status,
    onClickAction: CoroutineScope.(Action) -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 16.dp)
        .then(modifier),
) {
    StatusHeader(
        status.tooter,
        status.visibility,
        status.createdAt,
    )

    Spacer(Modifier.height(8.dp))

    HtmlText(
        status.content,
        style = MaterialTheme.typography.bodyLarge,
    )

    StatusActions(
        status,
        onClickAction,
    )
}

@Composable
private fun StatusHeader(
    tooter: Tooter,
    visibility: Status.Visibility,
    createdAt: LocalDateTime,
) = Row {
    var tooterIconSize by remember { mutableStateOf(0) }

    AsyncImage(
        tooter.avatar,
        //fallback = imageResources(getDrawables().icon_mastodon),
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
        trailing = { baselineModifier ->
            VisibilityIcon(
                visibility,
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
private fun VisibilityIcon(
    visibility: Status.Visibility,
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
        tint = MaterialTheme.colorScheme.outline,
    )
}


@Composable
private fun StatusActions(
    status: Status,
    onClick: CoroutineScope.(Action) -> Unit,
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
            modifier = Modifier.size(24.dp)
                .align(Alignment.CenterVertically)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onClick(scope) },
            tint = tint,
        )
        Text(
            count?.takeIf { it > 0 }?.toString() ?: "",
            modifier = Modifier.padding(horizontal = 4.dp)
                .align(Alignment.CenterVertically),
        )
    }
}
