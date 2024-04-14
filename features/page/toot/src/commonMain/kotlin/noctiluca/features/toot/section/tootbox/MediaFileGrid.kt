package noctiluca.features.toot.section.tootbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.icon.OverlayIcon
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.toot.model.MediaFile

private val MediaFileHeight = 240.dp

@Composable
internal fun MediaFileGrid(
    files: List<MediaFile>,
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
    file: MediaFile,
    onClickClear: (Int) -> Unit,
) {
    val image = (file as? MediaFile.Image) ?: return

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(MediaFileHeight),
    ) {
        AsyncImage(
            image.original,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
        )

        OverlayIcon(
            Icons.Default.Clear,
            contentDescription = "Clear #1",
            onClick = { onClickClear(0) },
        )
    }
}

@Composable
private fun MultipleMedia(
    files: List<MediaFile>,
    onClickClear: (Int) -> Unit,
) = BoxWithConstraints {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        files.filterIsInstance<MediaFile.Image>()
            .forEachIndexed { index, file ->
                item {
                    Box(
                        modifier = Modifier.width(MediaFileWidth())
                            .height(MediaFileHeight)
                    ) {
                        AsyncImage(
                            file.original,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                        )

                        OverlayIcon(
                            Icons.Default.Clear,
                            contentDescription = "Clear #${index + 1}",
                            onClick = { onClickClear(index) },
                        )
                    }
                }
            }
    }
}

@Suppress("MagicNumber")
@Composable
private fun BoxWithConstraintsScope.MediaFileWidth() = with(LocalDensity.current) {
    constraints.maxWidth.toDp() * 0.75F
}
