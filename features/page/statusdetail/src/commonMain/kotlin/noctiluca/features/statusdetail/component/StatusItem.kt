package noctiluca.features.statusdetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noctiluca.features.shared.atoms.text.HtmlText
import noctiluca.features.shared.status.Action
import noctiluca.features.shared.utils.border
import noctiluca.features.statusdetail.component.item.*
import noctiluca.features.statusdetail.component.item.Actions
import noctiluca.features.statusdetail.component.item.ConversationAxis
import noctiluca.features.statusdetail.component.item.Count
import noctiluca.features.statusdetail.component.item.CreatedAtAndVia
import noctiluca.features.statusdetail.component.item.Header
import noctiluca.features.statusdetail.component.item.Position
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.features.shared.status.Status as ComposableStatus

@Composable
internal fun StatusDetail(
    status: Status,
    onClickAvatar: (AccountId) -> Unit,
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
    Header(
        status.tooter,
        status.visibility,
        onClickAvatar,
    )

    Spacer(Modifier.height(8.dp))

    HtmlText(
        status.content,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 20.sp,
            lineHeight = 28.sp,
        ),
    )

    CreatedAtAndVia(
        status.createdAt,
        status.via,
    )
    HorizontalDivider()
    Count(
        reblogCount = status.reblogCount,
        favouriteCount = status.favouriteCount,
    )
    Actions(
        reblogged = status.reblogged,
        favourited = status.favourited,
        bookmarked = status.bookmarked,
        onClickAction = onClickAction,
    )
}

@Composable
internal fun StatusItem(
    status: Status,
    position: Position,
    onClickStatus: (StatusId) -> Unit,
    onClickAvatar: (AccountId) -> Unit,
    onClickAction: (Action) -> Unit,
) = Row(
    modifier = Modifier.clickable { onClickStatus(status.id) }
        .height(IntrinsicSize.Min)
        .padding(start = 8.dp),
) {
    ConversationAxis(position)

    ComposableStatus(
        status,
        onClickAvatar = { onClickAvatar(it) },
        onClickAction = { onClickAction(it) },
    )
}
