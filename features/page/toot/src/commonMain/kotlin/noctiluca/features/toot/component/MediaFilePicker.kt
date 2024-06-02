package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import noctiluca.model.media.MediaFile

internal expect class MediaFilePickerLauncher {
    fun launch()
}

@Composable
internal expect fun rememberMediaFilePickerLauncher(
    onSelect: (List<MediaFile>) -> Unit,
): MediaFilePickerLauncher
