package noctiluca.features.toot.model

import noctiluca.model.Uri

sealed class MediaFile {
    companion object {
        const val MAX_SELECTION_SIZE = 4
    }

    abstract val original: Uri
    abstract val mimeType: String

    data class Image(
        override val original: Uri,
        override val mimeType: String,
    ) : MediaFile()

    data class Video(
        override val original: Uri,
        override val mimeType: String,
    ) : MediaFile()

    data class Audio(
        override val original: Uri,
        override val mimeType: String,
    ) : MediaFile()

    data class Unknown(
        override val original: Uri,
        override val mimeType: String,
    ) : MediaFile()
}

internal expect val MediaFile.preview: Uri?