package noctiluca.features.shared.toot

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.card.FilledCard
import noctiluca.features.shared.toot.internal.TootTextArea
import noctiluca.model.status.Status

@Composable
fun FloatingTootCard(
    content: String?,
    warning: String?,
    visibility: Status.Visibility,
    enabled: Boolean,
    expanded: MutableState<Boolean>,
    onChangeContent: (String?) -> Unit,
    onChangeWarningText: (String?) -> Unit,
    onChangeVisibility: (Status.Visibility) -> Unit,
    onClickToot: () -> Unit,
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
        VisibilityChip(
            visibility,
            enabled = enabled,
            onChangeVisibility = onChangeVisibility,
        )

        Spacer(Modifier.weight(1F))

        IconButton(
            onClick = onClickOpenFullScreen,
            enabled = enabled,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Launch,
                contentDescription = "Open Full Toot Screen",
            )
        }
        IconButton(
            onClick = { expanded.value = false },
            enabled = enabled,
        ) {
            Icon(
                Icons.Default.Cancel,
                contentDescription = "Close Toot Card",
            )
        }
        Spacer(Modifier.width(4.dp))
    }

    TootTextArea(
        content ?: "",
        warning ?: "",
        enabled = enabled,
        borderColor = MaterialTheme.colorScheme.outline,
        onChangeContent = onChangeContent,
        onChangeWarningText = onChangeWarningText,
        onClickToot = onClickToot,
        modifier = Modifier.fillMaxWidth(),
    )
}
