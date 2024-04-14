package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import noctiluca.features.toot.model.MediaFile
import noctiluca.model.Uri

internal actual class MediaFilePickerLauncher {
    actual fun launch() = Unit
}

@Composable
internal actual fun rememberMediaFilePickerLauncher(
    onSelect: (List<MediaFile>) -> Unit,
): MediaFilePickerLauncher {
    return remember { MediaFilePickerLauncher() }
}
