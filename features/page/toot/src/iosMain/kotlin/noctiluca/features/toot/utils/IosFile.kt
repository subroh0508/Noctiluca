package noctiluca.features.toot.utils

import noctiluca.model.Uri
import platform.Foundation.NSURL
import platform.Foundation.pathExtension
import platform.UniformTypeIdentifiers.UTType

internal fun NSURL.toKmpUri() = path?.let(::Uri)

internal fun NSURL.getMimeType() = pathExtension?.let {
    UTType.typeWithFilenameExtension(it)
}?.preferredMIMEType
