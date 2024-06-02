package noctiluca.features.toot.component

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import noctiluca.features.toot.model.MEDIA_FILE_MAX_SELECTION_SIZE
import noctiluca.features.toot.utils.getMimeType
import noctiluca.features.toot.utils.toKmpUri
import noctiluca.model.media.MediaFile

internal actual class MediaFilePickerLauncher(
    private val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards android.net.Uri>>,
) {
    actual fun launch() = launcher.launch(
        PickVisualMediaRequest(),
    )
}

@Composable
internal actual fun rememberMediaFilePickerLauncher(
    onSelect: (List<MediaFile>) -> Unit,
): MediaFilePickerLauncher {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        PickMultipleVisualMedia(maxItems = MEDIA_FILE_MAX_SELECTION_SIZE),
    ) { result -> onSelect(result.map { it.toMediaFile(context) }) }

    return MediaFilePickerLauncher(launcher)
}

@OptIn(UnstableApi::class)
private fun android.net.Uri.toMediaFile(context: Context): MediaFile {
    val mimeType = getMimeType(context)

    return when {
        mimeType.startsWith(MimeTypes.BASE_TYPE_IMAGE) -> MediaFile.Image(
            toKmpUri(context),
            mimeType,
        )

        mimeType.startsWith(MimeTypes.BASE_TYPE_VIDEO) -> MediaFile.Video(
            toKmpUri(context),
            mimeType,
        )

        mimeType.startsWith(MimeTypes.BASE_TYPE_AUDIO) -> MediaFile.Audio(
            toKmpUri(context),
            mimeType,
        )

        else -> MediaFile.Unknown(toKmpUri(context), mimeType)
    }
}
