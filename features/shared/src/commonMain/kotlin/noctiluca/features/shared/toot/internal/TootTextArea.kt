package noctiluca.features.shared.toot.internal

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

internal val TootAreaPadding = 16.dp
private val OptionButtonsHorizontalPadding = 4.dp

private const val MAX_CONTENT_LENGTH = 500

@Composable
internal fun TootTextArea(
    content: String,
    warning: String? = null,
    onChangeContent: (String?) -> Unit,
    onChangeWarningText: (String?) -> Unit,
    textAreaModifier: Modifier = Modifier,
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
            modifier = Modifier.then(textAreaModifier)
                .fillMaxWidth()
                .padding(TootAreaPadding),
        )

        Divider(Modifier.padding(horizontal = TootAreaPadding))

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = OptionButtonsHorizontalPadding),
        ) {
            OptionButtons(
                isContentWarning,
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
    isContentWarning: Boolean,
    onToggleContentWarning: (Boolean) -> Unit,
) {
    val (warningIcon, warningTint) =
        if (isContentWarning) {
            Icons.Default.Warning to MaterialTheme.colorScheme.primary
        } else {
            Icons.Default.WarningAmber to LocalContentColor.current
        }

    Row {
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.Attachment,
                contentDescription = "File Picker",
            )
        }

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
