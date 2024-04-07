package noctiluca.features.shared.status.attachment

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.model.Uri
import noctiluca.model.status.Attachment

internal val GridHeight = 160.dp
internal const val COUNT_PER_GRID = 4

private val ImageModifier = Modifier.fillMaxSize()
    .clip(RoundedCornerShape(8.dp))

@Composable
internal fun ThumbnailGrid(
    attachments: List<Attachment>,
) = attachments.filter { it !is Attachment.Audio && it !is Attachment.Unknown }
    .chunked(COUNT_PER_GRID)
    .forEach { chunked -> ThumbnailGridItem(chunked) }

@Suppress("MagicNumber")
@Composable
private fun ThumbnailGridItem(
    attachments: List<Attachment>,
) {
    var openDialog by remember { mutableStateOf(false) }
    var index by remember { mutableStateOf(0) }

    if (openDialog) {
        AttachmentPreviewDialog(
            attachments,
            index,
            onDismissRequest = { openDialog = false },
        )
    }

    val previewUrls = attachments.map { it.previewUrl }
    val onClick: (Int) -> Unit = {
        openDialog = true
        index = it
    }

    when (previewUrls.size) {
        1 -> ThumbnailOne(previewUrls, onClick = onClick)
        2 -> ThumbnailTwo(previewUrls, onClick = onClick)
        3 -> ThumbnailThree(previewUrls, onClick = onClick)
        4 -> ThumbnailFour(previewUrls, onClick = onClick)
    }
}

@Composable
private fun ThumbnailOne(
    previewUrls: List<Uri>,
    onClick: (Int) -> Unit,
) = AsyncImage(
    previewUrls[0],
    contentScale = ContentScale.Crop,
    modifier = Modifier.clickable { onClick(0) }
        .fillMaxWidth()
        .height(GridHeight)
        .clip(RoundedCornerShape(8.dp)),
)

@Composable
private fun ThumbnailTwo(
    previewUrls: List<Uri>,
    onClick: (Int) -> Unit,
) = Row(
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight),
) {
    Box(
        modifier = Modifier.weight(1F)
            .clickable { onClick(0) },
    ) {
        AsyncImage(
            previewUrls[0],
            contentScale = ContentScale.Crop,
            modifier = ImageModifier,
        )
    }
    Spacer(Modifier.width(4.dp))
    Box(
        modifier = Modifier.weight(1F)
            .clickable { onClick(1) }
    ) {
        AsyncImage(
            previewUrls[1],
            contentScale = ContentScale.Crop,
            modifier = ImageModifier,
        )
    }
}

@Composable
private fun ThumbnailThree(
    previewUrls: List<Uri>,
    onClick: (Int) -> Unit,
) = Row(
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight),
) {
    Box(
        modifier = Modifier.weight(1F)
            .clickable { onClick(0) }
    ) {
        AsyncImage(
            previewUrls[0],
            contentScale = ContentScale.Crop,
            modifier = ImageModifier,
        )
    }
    Spacer(Modifier.width(4.dp))
    Column(
        modifier = Modifier.weight(1F)
            .fillMaxHeight(),
    ) {
        Box(
            modifier = Modifier.weight(1F)
                .clickable { onClick(1) }
        ) {
            AsyncImage(
                previewUrls[1],
                contentScale = ContentScale.Crop,
                modifier = ImageModifier,
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier.weight(1F)
                .clickable { onClick(2) }
        ) {
            AsyncImage(
                previewUrls[2],
                contentScale = ContentScale.Crop,
                modifier = ImageModifier,
            )
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun ThumbnailFour(
    previewUrls: List<Uri>,
    onClick: (Int) -> Unit,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight * 2),
) {
    ThumbnailTwo(
        previewUrls.slice(0..1),
        onClick = onClick,
    )
    Spacer(Modifier.height(4.dp))
    ThumbnailTwo(
        previewUrls.slice(2..3),
        onClick = { onClick(it + 2) },
    )
}
