package noctiluca.features.shared.toot

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.card.FilledCard
import noctiluca.features.shared.toot.internal.TootTextArea
import noctiluca.model.status.Status

@Composable
fun FloatingTootCard(
    content: MutableState<String?>,
    warning: MutableState<String?>,
    visibility: MutableState<Status.Visibility>,
    expanded: MutableState<Boolean>,
    onClickOpenFullScreen: () -> Unit = {},
    modifier: Modifier = Modifier,
) = FilledCard(
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
    Spacer(Modifier.height(8.dp))
    Row {
        Spacer(Modifier.width(16.dp))
        VisibilityChip(visibility)

        Spacer(Modifier.weight(1F))

        IconButton(
            onClick = onClickOpenFullScreen,
        ) {
            Icon(
                Icons.Default.Launch,
                contentDescription = "Open Full Toot Screen",
            )
        }
        IconButton(
            onClick = { expanded.value = false },
        ) {
            Icon(
                Icons.Default.Cancel,
                contentDescription = "Close Toot Card",
            )
        }
        Spacer(Modifier.width(4.dp))
    }

    TootTextArea(
        content.value ?: "",
        warning.value ?: "",
        borderColor = MaterialTheme.colorScheme.outline,
        onChangeContent = { content.value = it },
        onChangeWarningText = { warning.value = it },
        modifier = Modifier.fillMaxWidth(),
    )
}
