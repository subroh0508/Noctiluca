package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import noctiluca.features.toot.model.MediaFile
import noctiluca.model.Uri

internal expect class MediaFilePickerLauncher {
    fun launch()
}

@Composable
internal expect fun rememberMediaFilePickerLauncher(
    onSelect: (List<MediaFile>) -> Unit,
): MediaFilePickerLauncher
