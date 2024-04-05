package noctiluca.features.shared.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.shared.getCommonString
import noctiluca.model.Uri
import noctiluca.model.status.Attachment

private const val COUNT_PER_GRID = 4
private val GridHeight = 160.dp

@Composable
fun AttachmentPreview(
    sensitive: Boolean,
    attachments: List<Attachment>,
) {
    if (attachments.isEmpty()) {
        return
    }

    SensitiveContent(
        sensitive = sensitive,
    ) {
        Column {
            attachments.filter { it !is Attachment.Audio && it !is Attachment.Unknown }
                .chunked(COUNT_PER_GRID)
                .forEach { chunked ->
                    val previewUrls = chunked.map { it.previewUrl }

                    when (chunked.size) {
                        1 -> ThumbnailOne(previewUrls)
                        2 -> ThumbnailTwo(previewUrls)
                        3 -> ThumbnailThree(previewUrls)
                        4 -> ThumbnailFour(previewUrls)
                    }
                }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SensitiveContent(
    sensitive: Boolean,
    content: @Composable () -> Unit,
) {
    if (!sensitive) {
        content()
        return
    }

    var showContent by remember { mutableStateOf(false) }

    if (showContent) {
        content()
        return
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(GridHeight)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceBright),
    ) {
        SuggestionChip(
            onClick = { showContent = !showContent },
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
}

@Composable
private fun ThumbnailOne(
    previewUrls: List<Uri>,
) = AsyncImage(
    previewUrls[0],
    contentScale = ContentScale.Crop,
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight)
        .clip(RoundedCornerShape(8.dp)),
)

@Composable
private fun ThumbnailTwo(
    previewUrls: List<Uri>,
) = Row(
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight),
) {
    Box(modifier = Modifier.weight(1F)) {
        AsyncImage(
            previewUrls[0],
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
        )
    }
    Spacer(Modifier.width(4.dp))
    Box(modifier = Modifier.weight(1F)) {
        AsyncImage(
            previewUrls[1],
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
        )
    }
}

@Composable
private fun ThumbnailThree(
    previewUrls: List<Uri>,
) = Row(
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight),
) {
    Box(modifier = Modifier.weight(1F)) {
        AsyncImage(
            previewUrls[0],
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
        )
    }
    Spacer(Modifier.width(4.dp))
    Column(
        modifier = Modifier.weight(1F)
            .fillMaxHeight(),
    ) {
        Box(modifier = Modifier.weight(1F)) {
            AsyncImage(
                previewUrls[1],
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(modifier = Modifier.weight(1F)) {
            AsyncImage(
                previewUrls[2],
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
    }
}

@Composable
private fun ThumbnailFour(
    previewUrls: List<Uri>,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight * 2),
) {
    ThumbnailTwo(previewUrls.slice(0..1))
    Spacer(Modifier.height(4.dp))
    ThumbnailTwo(previewUrls.slice(2..3))
}
