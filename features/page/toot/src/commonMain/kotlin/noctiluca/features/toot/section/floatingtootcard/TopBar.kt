package noctiluca.features.toot.section.floatingtootcard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.toot.component.chip.VisibilityChip
import noctiluca.model.status.Status

@Composable
internal fun TopBar(
    visibility: Status.Visibility,
    enabled: Boolean,
    onChangeVisibility: (Status.Visibility) -> Unit,
    onClickOpenFullScreen: () -> Unit,
    onCloseCard: () -> Unit,
) = Row {
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
        onClick = onCloseCard,
        enabled = enabled,
    ) {
        Icon(
            Icons.Default.Cancel,
            contentDescription = "Close Toot Card",
        )
    }
    Spacer(Modifier.width(4.dp))
}
