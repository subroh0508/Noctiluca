package noctiluca.features.toot.model

import noctiluca.model.Uri
import noctiluca.model.media.LocalMediaFile

internal actual val LocalMediaFile.preview: Uri?
    get() = when (this) {
        is LocalMediaFile.Image -> original
        is LocalMediaFile.Video -> null
        else -> null
    }
