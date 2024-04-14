package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.Clock
import noctiluca.features.toot.model.MediaFile
import noctiluca.features.toot.utils.getMimeType
import noctiluca.features.toot.utils.toKmpUri
import noctiluca.model.Uri
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSItemProvider
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.pathExtension
import platform.Foundation.writeToURL
import platform.Photos.PHPhotoLibrary.Companion.sharedPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationAssetRepresentationModeCurrent
import platform.PhotosUI.PHPickerConfigurationSelectionOrdered
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
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
        mimeType.startsWith("image") -> MediaFile.Image(kmpUri, mimeType, toJpg())
        mimeType.startsWith("video") -> MediaFile.Video(kmpUri, mimeType, toJpg())
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
            val files = didFinishPicking.mapNotNull {
                val result = it as? PHPickerResult ?: return@mapNotNull null
                result.itemProvider.loadFileRepresentationForTypeIdentifier()?.toMediaFile()
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
    configuration.selectionLimit = 4

    return PHPickerViewController(configuration).apply {
        this.delegate = pickerDelegate
    }
}

private suspend fun NSItemProvider.loadFileRepresentationForTypeIdentifier(): NSURL? =
    suspendCancellableCoroutine { continuation ->
        loadFileRepresentationForTypeIdentifier(
            typeIdentifier = UTTypeImage.identifier,
        ) { url, error ->
            if (error != null || url == null) {
                println("Error: $error / $url")
                continuation.resume(null)
            } else {
                continuation.resume(buildTemporaryFileURL(url))
            }
        }
    }

private fun buildTemporaryFileURL(
    nsUrl: NSURL,
): NSURL {
    val extension = nsUrl.pathExtension().orEmpty()
    return NSURL.fileURLWithPath(
        "${NSTemporaryDirectory()}/${Clock.System.now().epochSeconds}.$extension",
    ).also { nsUrl.dataRepresentation.writeToURL(it, true) }
}

private fun NSURL.toJpg(): Uri {
    println("path #1: $absoluteString")
    var result: NSData? = null
    while (result == null) {
        val data = NSData.dataWithContentsOfURL(this)
        if (data != null) {
            result = data
        }
    }

    val uiImage = result.let(::UIImage)
    val path = "${NSTemporaryDirectory()}/${Clock.System.now().epochSeconds}.jpg"

    println("path #2: $path")
    NSFileManager.defaultManager().createFileAtPath(
        path,
        UIImageJPEGRepresentation(uiImage, 1.0),
        null,
    )

    return Uri(path)
}
