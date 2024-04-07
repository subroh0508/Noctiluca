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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.navigation.rememberAttachmentPreview
import noctiluca.features.shared.components.image.AsyncImage
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
    val navigator = LocalNavigator.current
    val previews = List(attachments.size) {
        rememberAttachmentPreview(attachments, it)
    }

    when (attachments.size) {
        1 -> ThumbnailOne(attachments, onClick = { navigator?.push(previews[it]) })
        2 -> ThumbnailTwo(attachments, onClick = { navigator?.push(previews[it]) })
        3 -> ThumbnailThree(attachments, onClick = { navigator?.push(previews[it]) })
        4 -> ThumbnailFour(attachments, onClick = { navigator?.push(previews[it]) })
    }
}

@Composable
private fun ThumbnailOne(
    attachments: List<Attachment>,
    onClick: (Int) -> Unit,
) = AsyncImage(
    attachments[0].url,
    contentScale = ContentScale.Crop,
    modifier = Modifier.clickable { onClick(0) }
        .fillMaxWidth()
        .height(GridHeight)
        .clip(RoundedCornerShape(8.dp)),
)

@Composable
private fun ThumbnailTwo(
    attachments: List<Attachment>,
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
            attachments[0].url,
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
            attachments[1].url,
            contentScale = ContentScale.Crop,
            modifier = ImageModifier,
        )
    }
}

@Composable
private fun ThumbnailThree(
    attachments: List<Attachment>,
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
            attachments[0].url,
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
                attachments[1].url,
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
                attachments[2].url,
                contentScale = ContentScale.Crop,
                modifier = ImageModifier,
            )
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun ThumbnailFour(
    attachments: List<Attachment>,
    onClick: (Int) -> Unit,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .height(GridHeight * 2),
) {
    ThumbnailTwo(
        attachments.slice(0..1),
        onClick = onClick,
    )
    Spacer(Modifier.height(4.dp))
    ThumbnailTwo(
        attachments.slice(2..3),
        onClick = { onClick(it + 2) },
    )
}
