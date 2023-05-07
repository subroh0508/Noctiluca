package noctiluca.features.shared.toot

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.divider.Divider
import noctiluca.features.components.atoms.textfield.TextArea
import noctiluca.features.components.getCommonString
import noctiluca.features.shared.status.VisibilityIcon
import noctiluca.status.model.Status

private val TootAreaPadding = 16.dp
private val OptionButtonsHorizontalPadding = 4.dp

private const val MAX_CONTENT_LENGTH = 500

@Composable
fun TootTextArea(
    content: String,
    warning: String? = null,
    visibility: Status.Visibility,
    onChangeContent: (String?) -> Unit,
    onChangeWarningText: (String?) -> Unit,
    onChangeVisibility: (Status.Visibility) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isContentWarning by remember { mutableStateOf(false) }
    val leastCount by remember(content, warning) {
        derivedStateOf {
            MAX_CONTENT_LENGTH - content.length - (warning ?: "").length
        }
    }

    Column(modifier) {
        WarningTextField(
            warning,
            isContentWarning,
            onChangeWarningText,
        )

        TextArea(
            content,
            onValueChange = { onChangeContent(it) },
            supportingText = getCommonString().toot_support_text,
            modifier = Modifier.fillMaxWidth()
                .padding(TootAreaPadding),
        )

        Divider(Modifier.padding(horizontal = TootAreaPadding))

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = OptionButtonsHorizontalPadding),
        ) {
            OptionButtons(
                visibility,
                isContentWarning,
                onChangeVisibility,
                onToggleContentWarning = {
                    isContentWarning = it
                    if (!it) {
                        onChangeWarningText(null)
                    }
                },
            )

            Spacer(Modifier.weight(1f))

            LeastTextCount(leastCount)

            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Toot",
                )
            }
        }
    }
}

@Composable
private fun WarningTextField(
    warning: String?,
    isContentWarning: Boolean,
    onValueChange: (String?) -> Unit,
) {
    if (!isContentWarning) {
        return
    }

    TextArea(
        warning ?: "",
        onValueChange = { onValueChange(it) },
        supportingText = getCommonString().toot_warning_support_text,
        modifier = Modifier.fillMaxWidth()
            .padding(TootAreaPadding),
    )

    Divider(Modifier.padding(horizontal = TootAreaPadding))
}

@Composable
private fun OptionButtons(
    visibility: Status.Visibility,
    isContentWarning: Boolean,
    onChangeVisibility: (Status.Visibility) -> Unit,
    onToggleContentWarning: (Boolean) -> Unit,
) {
    val (warningIcon, warningTint) =
        if (isContentWarning) {
            Icons.Default.Warning to MaterialTheme.colorScheme.primary
        } else {
            Icons.Default.WarningAmber to LocalContentColor.current
        }

    var expandedVisibilityMenu by remember { mutableStateOf(false) }

    Row {
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.Attachment,
                contentDescription = "File Picker",
            )
        }

        IconButton(onClick = { expandedVisibilityMenu = true }) {
            VisibilityIcon(visibility)
        }

        VisibleDropdownMenu(
            expandedVisibilityMenu,
            onDismissRequest = { expandedVisibilityMenu = false },
            onChangeVisibility = onChangeVisibility,
        )

        IconButton(onClick = { onToggleContentWarning(!isContentWarning) }) {
            Icon(
                warningIcon,
                contentDescription = "Content Warning",
                tint = warningTint,
            )
        }
    }
}

@Composable
private fun RowScope.LeastTextCount(
    leastCount: Int,
) = Text(
    leastCount.toString(),
    color = if (leastCount > 0) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.error
    },
    style = MaterialTheme.typography.bodySmall,
    modifier = Modifier.padding(end = 8.dp)
        .align(Alignment.CenterVertically),
)

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

@Composable
private fun Status.Visibility.label() = when (this) {
    Status.Visibility.PUBLIC -> getCommonString().visibility_public
    Status.Visibility.UNLISTED -> getCommonString().visibility_unlisted
    Status.Visibility.PRIVATE -> getCommonString().visibility_private
    Status.Visibility.DIRECT -> getCommonString().visibility_direct
}

@Composable
private fun Status.Visibility.supportText() = when (this) {
    Status.Visibility.PUBLIC -> getCommonString().visibility_public_support_text
    Status.Visibility.UNLISTED -> getCommonString().visibility_unlisted_support_text
    Status.Visibility.PRIVATE -> getCommonString().visibility_private_support_text
    Status.Visibility.DIRECT -> getCommonString().visibility_direct_support_text
}
