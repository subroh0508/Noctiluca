package noctiluca.features.toot.section.tootbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.icon.OverlayIcon
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.toot.getString
import noctiluca.features.toot.model.TootModel
import noctiluca.features.toot.model.preview
import noctiluca.model.media.LocalMediaFile

private val MediaFileHeight = 240.dp

@Composable
internal fun MediaFileGrid(
    files: List<TootModel.MediaFile>,
    onClickClear: (Int) -> Unit,
) {
    if (files.isEmpty()) {
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        when (files.size) {
            1 -> SingleMedia(files[0], onClickClear)
            else -> MultipleMedia(files, onClickClear)
        }
    }
}

@Composable
private fun SingleMedia(
    file: TootModel.MediaFile,
    onClickClear: (Int) -> Unit,
) = MediaPreview(
    0,
    file,
    onClickClear = onClickClear,
    modifier = Modifier.fillMaxWidth()
        .height(MediaFileHeight),
)

@Composable
private fun MultipleMedia(
    files: List<TootModel.MediaFile>,
    onClickClear: (Int) -> Unit,
) = BoxWithConstraints {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        files.forEachIndexed { index, file ->
            item {
                MediaPreview(
                    index,
                    file,
                    onClickClear = onClickClear,
                    modifier = Modifier.width(MediaFileWidth())
                        .height(MediaFileHeight)
                )
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun MediaPreview(
    index: Int,
    file: TootModel.MediaFile,
    onClickClear: (Int) -> Unit,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier,
) {
    AsyncImage(
        file.previewUri,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
    )

    if (file is TootModel.MediaFile.Uploading) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.3F)),
        )

        LinearProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
                .padding(horizontal = 8.dp),
        )
    }

    if (file is TootModel.MediaFile.Failed) {
        ErrorMessage()
    }

    OverlayIcon(
        Icons.Default.Clear,
        contentDescription = "Clear #${index + 1}",
        onClick = { onClickClear(index) },
    )
}

@Suppress("MagicNumber")
@Composable
private fun BoxScope.ErrorMessage() = CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.onError,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(color = MaterialTheme.colorScheme.error.copy(alpha = 0.8F))
            .padding(8.dp),
    ) {
        Icon(Icons.Default.Warning, contentDescription = null)
        Spacer(Modifier.width(4.dp))
        Text(
            getString().toot_media_uploading_failure,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1F),
        )
    }
}

@Suppress("MagicNumber")
@Composable
private fun BoxWithConstraintsScope.MediaFileWidth() = with(LocalDensity.current) {
    constraints.maxWidth.toDp() * 0.75F
}
