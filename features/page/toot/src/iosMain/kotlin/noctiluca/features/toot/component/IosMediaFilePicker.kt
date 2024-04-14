package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.Clock
import noctiluca.features.toot.model.MediaFile
import noctiluca.features.toot.utils.getMimeType
import noctiluca.features.toot.utils.toKmpUri
import platform.Foundation.NSFileManager
import platform.Foundation.NSItemProvider
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.pathExtension
import platform.Photos.PHPhotoLibrary.Companion.sharedPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationAssetRepresentationModeCurrent
import platform.PhotosUI.PHPickerConfigurationSelectionOrdered
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject
import kotlin.coroutines.resume

internal actual class MediaFilePickerLauncher(
    private val pickerDelegate: PHPickerViewControllerDelegateProtocol,
) {

    actual fun launch() {
        val pickerViewController = createPHPickerViewController(pickerDelegate)

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            pickerViewController,
            true,
            null,
        )
    }
}

@Composable
internal actual fun rememberMediaFilePickerLauncher(
    onSelect: (List<MediaFile>) -> Unit,
): MediaFilePickerLauncher {
    val coroutineScope = rememberCoroutineScope()
    val delegate = remember { PHPickerDelegate(coroutineScope, onSelect) }

    return remember { MediaFilePickerLauncher(delegate) }
}

private fun NSURL.toMediaFile(): MediaFile {
    val mimeType = getMimeType() ?: error("Unknown MIME-Type: $path")
    val kmpUri = toKmpUri() ?: error("Unknown path: $path")

    return when {
        mimeType.startsWith("image") -> MediaFile.Image(kmpUri, mimeType)
        mimeType.startsWith("video") -> MediaFile.Video(kmpUri, mimeType)
        mimeType.startsWith("audio") -> MediaFile.Audio(kmpUri, mimeType)
        else -> MediaFile.Unknown(kmpUri, mimeType)
    }
}

private class PHPickerDelegate(
    private val coroutineScope: CoroutineScope,
    private val onSelect: (List<MediaFile>) -> Unit,
) : NSObject(), PHPickerViewControllerDelegateProtocol {
    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>,
    ) {
        picker.dismissViewControllerAnimated(true, null)

        coroutineScope.launch {
            val files = didFinishPicking.mapIndexedNotNull { index, item ->
                val result = item as? PHPickerResult ?: return@mapIndexedNotNull null
                result.itemProvider
                    .loadFileRepresentationForTypeIdentifier(index)
                    ?.toMediaFile()
            }

            onSelect(files)
        }
    }
}

private fun createPHPickerViewController(
    pickerDelegate: PHPickerViewControllerDelegateProtocol,
): PHPickerViewController {
    val configuration = PHPickerConfiguration(sharedPhotoLibrary())

    configuration.filter = PHPickerFilter.anyFilterMatchingSubfilters(
        listOf(PHPickerFilter.imagesFilter, PHPickerFilter.videosFilter),
    )
    configuration.preferredAssetRepresentationMode =
        PHPickerConfigurationAssetRepresentationModeCurrent
    configuration.selection = PHPickerConfigurationSelectionOrdered
    configuration.selectionLimit = MediaFile.MAX_SELECTION_SIZE.toLong()

    return PHPickerViewController(configuration).apply {
        this.delegate = pickerDelegate
    }
}

private suspend fun NSItemProvider.loadFileRepresentationForTypeIdentifier(
    index: Int,
): NSURL? = suspendCancellableCoroutine { continuation ->
    loadFileRepresentationForTypeIdentifier(
        typeIdentifier = UTTypeImage.identifier,
    ) { url, error ->
        if (error != null || url == null) {
            println("Error: $error / $url")
            continuation.resume(null)
        } else {
            continuation.resume(buildTemporaryFileURL(url, index))
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun buildTemporaryFileURL(
    nsUrl: NSURL,
    index: Int,
): NSURL {
    val extension = nsUrl.pathExtension().orEmpty()
    val path = "${NSTemporaryDirectory()}${Clock.System.now().epochSeconds}_$index.$extension"

    return NSURL.fileURLWithPath(
        path,
    ).also { NSFileManager.defaultManager().copyItemAtURL(nsUrl, it, null) }
}
