package noctiluca.features.toot.model

import kotlinx.datetime.Clock
import noctiluca.features.toot.utils.toKmpUri
import noctiluca.model.Uri
import noctiluca.model.extensions.readData
import noctiluca.model.media.LocalMediaFile
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

internal actual val LocalMediaFile.preview: Uri?
    get() = when (this) {
        is LocalMediaFile.Image -> buildJpgURL(original).toKmpUri()
        is LocalMediaFile.Video -> null
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
