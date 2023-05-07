package noctiluca.features.timeline.organisms.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.components.atoms.clickable
import noctiluca.features.shared.toot.TootTextArea
import noctiluca.status.model.Status

@Composable
internal fun BoxScope.TootCard(
    modifier: Modifier = Modifier,
) {
    val expanded = remember { mutableStateOf(false) }

    if (expanded.value) {
        FloatingTootCard(
            remember { mutableStateOf(null) },
            remember { mutableStateOf(null) },
            remember { mutableStateOf(Status.Visibility.PUBLIC) },
            expanded,
            modifier = modifier,
        )

        return
    }

    FloatingActionButton(
        onClick = { expanded.value = true },
        modifier = Modifier.padding(16.dp)
            .align(Alignment.BottomEnd),
    ) {
        Icon(
            Icons.Default.Edit,
            contentDescription = "Expand Toot Card",
        )
    }
}

@Composable
private fun FloatingTootCard(
    content: MutableState<String?>,
    warning: MutableState<String?>,
    visibility: MutableState<Status.Visibility>,
    expanded: MutableState<Boolean>,
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
    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.padding(horizontal = 16.dp)
            .align(Alignment.End),
    ) {
        Icon(
            Icons.Default.Launch,
            contentDescription = "Open Full Toot Screen",
            modifier = Modifier.clickable(
                noRipple = true,
                onClick = {},
            ),
        )
        Spacer(Modifier.width(16.dp))
        Icon(
            Icons.Default.Cancel,
            contentDescription = "Close Toot Card",
            modifier = Modifier.clickable(
                noRipple = true,
                onClick = { expanded.value = false },
            ),
        )
    }

    TootTextArea(
        content.value ?: "",
        warning.value ?: "",
        visibility.value,
        onChangeContent = { content.value = it },
        onChangeWarningText = { warning.value = it },
        onChangeVisibility = { visibility.value = it },
        modifier = Modifier.fillMaxWidth(),
    )
}
