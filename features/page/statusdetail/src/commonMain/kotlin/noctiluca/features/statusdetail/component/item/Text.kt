package noctiluca.features.statusdetail.component.item

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import noctiluca.features.shared.utils.format
import noctiluca.features.shared.utils.toYearMonthDayTime
import noctiluca.features.statusdetail.getString
import noctiluca.model.status.Status

@Composable
internal fun CreatedAtAndVia(
    createdAt: LocalDateTime,
    via: Status.Via?,
) = CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.outline,
    LocalTextStyle provides MaterialTheme.typography.titleSmall,
) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(createdAt.toYearMonthDayTime())
        Spacer(Modifier.width(8.dp))
        ViaText(via)
    }
}

@Composable
private fun ViaText(
    via: Status.Via?,
) {
    if (via == null) {
        return
    }

    Text("･")
    Spacer(Modifier.width(8.dp))
    Text("via ${via.name}")
}

@Composable
internal fun Count(
    reblogCount: Int,
    favouriteCount: Int,
) {
    if (reblogCount == 0 && favouriteCount == 0) {
        return
    }

    Column(Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            CountText(reblogCount, getString().status_detail_boost)
            CountText(favouriteCount, getString().status_detail_favourite)
        }
        HorizontalDivider()
    }
}

@Composable
private fun RowScope.CountText(
    count: Int,
    label: String,
) {
    if (count == 0) {
        return
    }

    Text(
        format(count),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
        modifier = Modifier.alignByBaseline(),
    )
    Text(
        label,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.alignByBaseline(),
    )
    Spacer(modifier = Modifier.width(8.dp))
}
