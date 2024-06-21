package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import noctiluca.model.media.LocalMediaFile

internal actual class MediaFilePickerLauncher {
    actual fun launch() = Unit
}

@Composable
internal actual fun rememberMediaFilePickerLauncher(
    onSelect: (List<LocalMediaFile>) -> Unit,
): MediaFilePickerLauncher {
    return remember { MediaFilePickerLauncher() }
}
