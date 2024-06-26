package noctiluca.features.toot.component.chip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import noctiluca.features.shared.status.VisibilityIcon
import noctiluca.model.status.Status

@Composable
internal fun VisibilityChip(
    visibility: Status.Visibility,
    enabled: Boolean,
    onChangeVisibility: (Status.Visibility) -> Unit,
) {
    var expandedVisibilityMenu by remember { mutableStateOf(false) }

    Box {
        AssistChip(
            enabled = enabled,
            onClick = { expandedVisibilityMenu = true },
            label = { Text(visibility.label()) },
            leadingIcon = { VisibilityIcon(visibility) },
        )

        VisibleDropdownMenu(
            expandedVisibilityMenu,
            onDismissRequest = { expandedVisibilityMenu = false },
            onChangeVisibility = onChangeVisibility,
        )
    }
}

@Composable
private fun VisibleDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onChangeVisibility: (Status.Visibility) -> Unit,
) = DropdownMenu(
    expanded = expanded,
    onDismissRequest = onDismissRequest,
) {
    listOf(
        Status.Visibility.PUBLIC,
        Status.Visibility.UNLISTED,
        Status.Visibility.PRIVATE,
    ).forEach { visibility ->
        DropdownMenuItem(
            text = {
                Column {
                    Text(visibility.label())
                    Text(
                        visibility.supportText(),
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            },
            leadingIcon = { VisibilityIcon(visibility) },
            onClick = {
                onDismissRequest()
                onChangeVisibility(visibility)
            },
        )
    }
}
