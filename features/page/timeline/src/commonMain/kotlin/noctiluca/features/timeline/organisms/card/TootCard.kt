package noctiluca.features.timeline.organisms.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.toot.FloatingTootCard
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
