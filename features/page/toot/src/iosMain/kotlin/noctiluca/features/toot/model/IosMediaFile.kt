package noctiluca.features.toot.model

import kotlinx.datetime.Clock
import noctiluca.features.toot.utils.toKmpUri
import noctiluca.model.Uri
import noctiluca.model.media.MediaFile
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

internal actual val MediaFile.preview: Uri?
    get() = when (this) {
        is MediaFile.Image -> buildJpgURL(original).toKmpUri()
        is MediaFile.Video -> null
        else -> null
    }

private fun buildJpgURL(original: Uri): NSURL {
    val nsUrl = NSURL.fileURLWithPath(original.value)
    val fileName = nsUrl.path
        ?.split("/")
        ?.lastOrNull()
        ?.split(".")
        ?.firstOrNull()

    val path = "${NSTemporaryDirectory()}${fileName ?: Clock.System.now().epochSeconds}.jpg"

    NSFileManager.defaultManager().createFileAtPath(
        path,
        UIImageJPEGRepresentation(UIImage(nsUrl.readData()), 1.0),
        null,
    )

    return NSURL.fileURLWithPath(path)
}

private fun NSURL.readData(): NSData {
    var result: NSData? = null
    while (result == null) {
        val data = NSData.dataWithContentsOfURL(this)
        if (data != null) {
            result = data
        }
    }

    return result
}
