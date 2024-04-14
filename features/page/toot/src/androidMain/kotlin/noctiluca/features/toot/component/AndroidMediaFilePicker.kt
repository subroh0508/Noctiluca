package noctiluca.features.toot.component

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import noctiluca.features.toot.utils.toKmpUri
import noctiluca.model.Uri

internal actual class MediaFilePickerLauncher(
    private val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards android.net.Uri>>,
) {
    actual fun launch() = launcher.launch(
        PickVisualMediaRequest(),
    )
}

@Composable
internal actual fun rememberMediaFilePickerLauncher(
    onSelect: (List<Uri>) -> Unit,
): MediaFilePickerLauncher {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        PickMultipleVisualMedia(maxItems = 4),
    ) { result -> onSelect(result.map { it.toKmpUri(context) }) }

    return MediaFilePickerLauncher(launcher)
}
