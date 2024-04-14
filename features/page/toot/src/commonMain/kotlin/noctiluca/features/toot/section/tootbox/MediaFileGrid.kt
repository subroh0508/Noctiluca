package noctiluca.features.toot.section.tootbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.toot.model.MediaFile

@Composable
internal fun MediaFileGrid(
    files: List<MediaFile>,
) {
    if (files.isEmpty()) {
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
    ) {
        files.forEach { file ->
            AsyncImage(
                file.url,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
    }
}
