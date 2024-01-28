package noctiluca.features.accountdetail.component.headline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.getString
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun RelationshipStateChips(
    relationships: Relationships,
    modifier: Modifier = Modifier,
) = Column(
    horizontalAlignment = Alignment.End,
    modifier = modifier.padding(bottom = 8.dp, end = 16.dp),
) {
    if (relationships.muting) {
        RelationshipStateChip(Icons.AutoMirrored.Filled.VolumeOff, getString().account_detail_muting)
    }

    relationships.assets()?.let { (label, icon) ->
        Spacer(modifier = Modifier.height(4.dp))
        RelationshipStateChip(icon, label)
    }
}

@Composable
private fun RelationshipStateChip(
    imageVector: ImageVector,
    label: String,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.height(AssistChipDefaults.Height)
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8F),
            shape = RoundedCornerShape(8.dp),
        )
        .padding(start = 8.dp, end = 16.dp),
) {
    Icon(
        imageVector,
        contentDescription = null,
        modifier = Modifier.size(18.dp),
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        label,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
private fun Relationships.assets() = when {
    followedBy -> getString().account_detail_followed to Icons.Default.Check
    requested -> getString().account_detail_sending_follow_request to Icons.AutoMirrored.Filled.Send
    blocking && !blockedBy -> getString().account_detail_blocking to Icons.Default.Block
    !blocking && blockedBy -> getString().account_detail_blocked to Icons.Default.Block
    else -> null
}
