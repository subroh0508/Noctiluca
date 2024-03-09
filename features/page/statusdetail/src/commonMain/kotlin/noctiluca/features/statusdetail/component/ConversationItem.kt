package noctiluca.features.statusdetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.status.Action
import noctiluca.features.shared.status.Status
import noctiluca.features.statusdetail.component.item.ConversationAxis
import noctiluca.features.statusdetail.component.item.Position
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.status.Status

@Composable
internal fun ConversationItem(
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

    Status(
        status,
        onClickAvatar = { onClickAvatar(it) },
        onClickAction = { onClickAction(it) },
    )
}
