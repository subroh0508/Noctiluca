package noctiluca.features.toot.section.tootbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.toot.model.MediaFile

private val MediaFileHeight = 240.dp

@Composable
internal fun MediaFileGrid(
    files: List<MediaFile>,
) {
    if (files.isEmpty()) {
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        when (files.size) {
            1 -> SingleMedia(files[0])
            else -> MultipleMedia(files)
        }
    }
}

@Composable
private fun SingleMedia(
    file: MediaFile,
) = AsyncImage(
    file.url,
    contentScale = ContentScale.Crop,
    modifier = Modifier.fillMaxWidth()
        .height(MediaFileHeight)
        .clip(RoundedCornerShape(8.dp)),
)

@Composable
private fun MultipleMedia(
    files: List<MediaFile>
) = BoxWithConstraints {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        files.forEach { file ->
            item {
                AsyncImage(
                    file.url,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(MediaFileWidth())
                        .height(MediaFileHeight)
                        .clip(RoundedCornerShape(8.dp)),
                )
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun BoxWithConstraintsScope.MediaFileWidth() = with(LocalDensity.current) {
    constraints.maxWidth.toDp() * 0.75F
}
