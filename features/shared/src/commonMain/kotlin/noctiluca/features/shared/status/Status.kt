package noctiluca.features.shared.status

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.atoms.text.RelativeTime
import noctiluca.features.components.utils.baseline
import noctiluca.features.components.utils.toDp
import noctiluca.features.shared.account.TooterName
import noctiluca.status.model.Status
import noctiluca.status.model.Tooter

@Composable
fun Status(
    status: Status,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
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
private fun MenuIcon(
    modifier: Modifier,
) = Icon(
    Icons.Default.MoreHoriz,
    contentDescription = "menu",
    modifier,
)

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
