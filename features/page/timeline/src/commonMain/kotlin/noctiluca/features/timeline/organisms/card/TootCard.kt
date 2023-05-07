package noctiluca.features.timeline.organisms.card

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.shared.toot.TootTextArea
import noctiluca.status.model.Status

@Composable
internal fun TootCard(
    modifier: Modifier,
) {
    var content by remember { mutableStateOf<String?>(null) }
    var warning by remember { mutableStateOf<String?>(null) }
    var visibility by remember { mutableStateOf(Status.Visibility.PUBLIC) }

    FilledCard(
        modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme
                .surfaceVariant
                .copy(alpha = 0.9F),
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp,
        ),
    ) {
        TootTextArea(
            content ?: "",
            warning ?: "",
            visibility,
            onChangeContent = { content = it },
            onChangeWarningText = { warning = it },
            onChangeVisibility = { visibility = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
