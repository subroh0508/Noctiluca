package noctiluca.features.shared.toot.internal

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.textfield.TextArea
import noctiluca.features.shared.getCommonString
import noctiluca.features.shared.utils.MAX_CONTENT_LENGTH
import noctiluca.features.shared.utils.isEnabledToot

internal val TootAreaPadding = 16.dp
private val OptionButtonsHorizontalPadding = 4.dp

private val LocalEnabledTootTextArea = compositionLocalOf { true }

@Composable
internal fun TootTextArea(
    content: String,
    warning: String? = null,
    enabled: Boolean,
    borderColor: Color = DividerDefaults.color,
    onChangeContent: (String?) -> Unit,
    onChangeWarningText: (String?) -> Unit,
    onClickToot: () -> Unit,
    textAreaModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    var isContentWarning by remember { mutableStateOf(false) }
    val leastCount by remember(content, warning) {
        derivedStateOf {
            MAX_CONTENT_LENGTH - content.length - (warning ?: "").length
        }
    }

    CompositionLocalProvider(
        LocalEnabledTootTextArea provides enabled,
    ) {
        Column(modifier) {
            Column(textAreaModifier) {
                WarningTextField(
                    warning,
                    isContentWarning,
                    borderColor,
                    onChangeWarningText,
                )

                TextArea(
                    content,
                    enabled = LocalEnabledTootTextArea.current,
                    onValueChange = { onChangeContent(it) },
                    supportingText = getCommonString().toot_support_text,
                    modifier = Modifier.fillMaxWidth()
                        .padding(TootAreaPadding),
                )
            }

            HorizontalDivider(
                color = borderColor,
                modifier = Modifier.padding(horizontal = TootAreaPadding),
            )

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

                IconButton(
                    onClick = onClickToot,
                    enabled = LocalEnabledTootTextArea.current && isEnabledToot(content),
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Toot",
                    )
                }
            }
        }
    }
}

@Composable
private fun WarningTextField(
    warning: String?,
    isContentWarning: Boolean,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    onValueChange: (String?) -> Unit,
) {
    if (!isContentWarning) {
        return
    }

    TextArea(
        warning ?: "",
        enabled = LocalEnabledTootTextArea.current,
        onValueChange = { onValueChange(it) },
        supportingText = getCommonString().toot_warning_support_text,
        modifier = Modifier.fillMaxWidth()
            .padding(TootAreaPadding),
    )

    HorizontalDivider(
        color = borderColor,
        modifier = Modifier.padding(horizontal = TootAreaPadding),
    )
}

@Composable
private fun OptionButtons(
    isContentWarning: Boolean,
    onToggleContentWarning: (Boolean) -> Unit,
) {
    val (warningIcon, warningIconColors) =
        if (isContentWarning) {
            Icons.Default.Warning to IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            )
        } else {
            Icons.Default.WarningAmber to IconButtonDefaults.iconButtonColors()
        }

    Row {
        IconButton(
            onClick = {},
            enabled = LocalEnabledTootTextArea.current,
        ) {
            Icon(
                Icons.Default.Attachment,
                contentDescription = "File Picker",
            )
        }

        IconButton(
            onClick = { onToggleContentWarning(!isContentWarning) },
            enabled = LocalEnabledTootTextArea.current,
            colors = warningIconColors,
        ) {
            Icon(
                warningIcon,
                contentDescription = "Content Warning",
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
