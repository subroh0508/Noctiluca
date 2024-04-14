package noctiluca.features.toot.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import noctiluca.features.toot.model.MediaFile
import noctiluca.features.toot.utils.getMimeType
import noctiluca.features.toot.utils.toKmpUri
import platform.Foundation.NSURL
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
    val delegate = remember { PHPickerDelegate(onSelect) }

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
    private val onSelect: (List<MediaFile>) -> Unit,
) : NSObject(), PHPickerViewControllerDelegateProtocol {
    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>,
    ) {
        val files: MutableList<MediaFile> = mutableListOf()

        picker.dismissViewControllerAnimated(true, null)
        println("didFinishPicking: $didFinishPicking")
        didFinishPicking.forEach {
            val result = it as? PHPickerResult ?: return@forEach

            result.itemProvider.loadFileRepresentationForTypeIdentifier(
                typeIdentifier = UTTypeImage.identifier,
            ) { url, error ->
                if (error != null) {
                    println("Error: $error")
                    return@loadFileRepresentationForTypeIdentifier
                }

                url?.toMediaFile()?.let(files::add)
            }
        }

        onSelect(files)
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
