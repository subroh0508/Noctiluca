package noctiluca.features.statusdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import noctiluca.features.shared.status.Action
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import androidx.compose.material3.Divider as MaterialDivider
import noctiluca.features.shared.status.Status as ComposableStatus

internal enum class Position { TOP, MIDDLE, BOTTOM }

@Composable
internal fun StatusItem(
    status: Status,
    position: Position,
    onClickStatus: CoroutineScope.(StatusId) -> Unit,
    onClickAction: CoroutineScope.(Action) -> Unit,
) = Row(
    modifier = Modifier.height(IntrinsicSize.Min)
        .padding(start = 8.dp),
) {
    when (position) {
        Position.TOP -> TimelineAxisTop()
        Position.MIDDLE -> TimelineAxisMiddle()
        Position.BOTTOM -> TimelineAxisBottom()
    }

    ComposableStatus(status, onClickStatus, onClickAction)
}

@Composable
private fun TimelineAxisTop() = Box(
    modifier = Modifier.fillMaxHeight(),
) {
    MaterialDivider(
        modifier = Modifier.padding(
            top = 16.dp,
            start = 7.dp,
        )
            .fillMaxHeight()
            .width(2.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    TimelineDot()
}

@Composable
private fun TimelineAxisMiddle() = Box(
    modifier = Modifier.fillMaxHeight(),
) {
    MaterialDivider(
        modifier = Modifier.padding(start = 7.dp)
            .fillMaxHeight()
            .width(2.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    TimelineDot()
}

@Composable
private fun TimelineAxisBottom() = Box {
    MaterialDivider(
        modifier = Modifier.padding(start = 7.dp)
            .height(16.dp)
            .width(2.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    TimelineDot()
}

@Composable
private fun TimelineDot() = Box(
    modifier = Modifier.offset(y = 16.dp)
        .size(16.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
)
