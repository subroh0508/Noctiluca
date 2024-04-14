package noctiluca.features.toot.model

import noctiluca.model.Uri

internal actual val MediaFile.preview: Uri?
    get() = when (this) {
        is MediaFile.Image -> original
        is MediaFile.Video -> null
        else -> null
    }
