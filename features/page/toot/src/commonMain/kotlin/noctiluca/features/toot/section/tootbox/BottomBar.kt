package noctiluca.features.toot.section.tootbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.utils.isEnabledToot
import noctiluca.features.toot.component.rememberMediaFilePickerLauncher
import noctiluca.features.toot.model.MAX_CONTENT_LENGTH
import noctiluca.model.media.LocalMediaFile

private val OptionButtonsHorizontalPadding = 4.dp

@Composable
internal fun BottomBar(
    content: String,
    warning: String? = null,
    isContentWarning: Boolean,
    enabled: Boolean,
    onSelectFiles: (List<LocalMediaFile>) -> Unit,
    onToggleContentWarning: (Boolean) -> Unit,
    onClickToot: () -> Unit,
) {
    val leastCount by remember(content, warning) {
        derivedStateOf {
            MAX_CONTENT_LENGTH - content.length - (warning ?: "").length
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = OptionButtonsHorizontalPadding),
    ) {
        OptionButtons(
            isContentWarning,
            enabled = enabled,
            onSelectFiles = onSelectFiles,
            onToggleContentWarning = onToggleContentWarning,
        )

        Spacer(Modifier.weight(1f))

        LeastTextCount(leastCount)

        IconButton(
            onClick = onClickToot,
            enabled = enabled && isEnabledToot(content),
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Toot",
            )
        }
    }
}

@Composable
private fun OptionButtons(
    isContentWarning: Boolean,
    enabled: Boolean,
    onSelectFiles: (List<LocalMediaFile>) -> Unit,
    onToggleContentWarning: (Boolean) -> Unit,
) {
    val launcher = rememberMediaFilePickerLauncher(onSelectFiles)
    val (warningIcon, warningIconColors) = IconResources(isContentWarning)

    Row {
        IconButton(
            onClick = { launcher.launch() },
            enabled = enabled,
        ) {
            Icon(
                Icons.Default.Attachment,
                contentDescription = "File Picker",
            )
        }

        IconButton(
            onClick = { onToggleContentWarning(!isContentWarning) },
            enabled = enabled,
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
private fun IconResources(isContentWarning: Boolean) =
    if (isContentWarning) {
        Icons.Default.Warning to IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        )
    } else {
        Icons.Default.WarningAmber to IconButtonDefaults.iconButtonColors()
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
