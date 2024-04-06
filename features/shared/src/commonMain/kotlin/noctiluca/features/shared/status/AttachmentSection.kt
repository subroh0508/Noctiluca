package noctiluca.features.shared.status

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.getCommonString
import noctiluca.features.shared.status.attachment.COUNT_PER_GRID
import noctiluca.features.shared.status.attachment.GridHeight
import noctiluca.features.shared.status.attachment.ThumbnailGrid
import noctiluca.model.status.Attachment

@Composable
fun AttachmentSection(
    sensitive: Boolean,
    attachments: List<Attachment>,
) {
    if (attachments.isEmpty()) {
        return
    }

    SensitiveContent(
        sensitive = sensitive,
        height = attachments.gridHeight,
    ) {
        Column {
            ThumbnailGrid(attachments)
            Spacer(Modifier.height(8.dp))
        }
    }
}

private val List<Attachment>.gridHeight get() = GridHeight * ((size / COUNT_PER_GRID) + 1)

@Composable
private fun SensitiveContent(
    sensitive: Boolean,
    height: Dp,
    content: @Composable () -> Unit,
) {
    if (!sensitive) {
        content()
        return
    }

    var showContent by remember { mutableStateOf(false) }

    if (showContent) {
        OverlayHideIcon(
            height = height,
            onHideContent = { showContent = false },
            content = content,
        )
        return
    }

    SensitiveCover(
        height = height,
        onShowContent = { showContent = true },
    )
}

@Composable
private fun OverlayHideIcon(
    height: Dp,
    onHideContent: () -> Unit,
    content: @Composable () -> Unit,
) = Box(
    modifier = Modifier.fillMaxWidth()
        .height(height),
) {
    content()

    Box(
        modifier = Modifier.clickable { onHideContent() }
            .offset(x = 4.dp, y = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(0.6F))
            .padding(4.dp),
    ) {
        Icon(
            Icons.Default.VisibilityOff,
            contentDescription = "Hide Content",
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun SensitiveCover(
    height: Dp,
    onShowContent: () -> Unit,
) = Box(
    modifier = Modifier.clickable { onShowContent() }
        .fillMaxWidth()
        .height(height)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surfaceBright),
) {
    SuggestionChip(
        onClick = onShowContent,
        label = {
            Text(
                getCommonString().attachment_sensitive,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        border = null,
        modifier = Modifier.align(Alignment.Center),
    )
}
