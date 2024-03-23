package noctiluca.features.statusdetail.component.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.account.TooterName
import noctiluca.features.shared.components.clickable
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.shared.components.image.imageResources
import noctiluca.features.shared.getDrawables
import noctiluca.features.shared.status.VisibilityIcon
import noctiluca.features.shared.utils.toDp
import noctiluca.model.AccountId
import noctiluca.model.account.Account
import noctiluca.model.status.Status

@Composable
internal fun Header(
    tooter: Account,
    visibility: Status.Visibility,
    onClickAvatar: (AccountId) -> Unit,
) = Row {
    var tooterIconSize by remember { mutableStateOf(0) }

    AsyncImage(
        tooter.avatar,
        fallback = imageResources(getDrawables().icon_mastodon),
        modifier = Modifier.size(tooterIconSize.toDp())
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClickAvatar(tooter.id) },
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
