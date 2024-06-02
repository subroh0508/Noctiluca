package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import noctiluca.model.media.LocalMediaFile

internal expect class MediaFilePickerLauncher {
    fun launch()
}

@Composable
internal expect fun rememberMediaFilePickerLauncher(
    onSelect: (List<LocalMediaFile>) -> Unit,
): MediaFilePickerLauncher
