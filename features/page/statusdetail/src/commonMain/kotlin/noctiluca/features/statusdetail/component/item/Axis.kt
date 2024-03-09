package noctiluca.features.statusdetail.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

internal enum class Position { TOP, MIDDLE, BOTTOM }

@Composable
internal fun ConversationAxis(
    position: Position,
) = when (position) {
    Position.TOP -> AxisTop()
    Position.MIDDLE -> AxisMiddle()
    Position.BOTTOM -> AxisBottom()
}

@Composable
private fun AxisTop() = Box(
    modifier = Modifier.fillMaxHeight(),
) {
    VerticalDivider(
        modifier = Modifier.padding(
            top = 16.dp,
            start = 7.dp,
        )
            .width(2.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    Dot()
}

@Composable
private fun AxisMiddle() = Box(
    modifier = Modifier.fillMaxHeight(),
) {
    VerticalDivider(
        modifier = Modifier.padding(start = 7.dp)
            .width(2.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    Dot()
}

@Composable
private fun AxisBottom() = Box {
    VerticalDivider(
        modifier = Modifier.padding(start = 7.dp)
            .height(16.dp)
            .width(2.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    Dot()
}

@Composable
private fun Dot() = Box(
    modifier = Modifier.offset(y = 16.dp)
        .size(16.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
)
