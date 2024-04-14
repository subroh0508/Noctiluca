package noctiluca.features.toot.model

import noctiluca.model.Uri

sealed class MediaFile {
    abstract val original: Uri
    abstract val mimeType: String

    data class Image(
        override val original: Uri,
        override val mimeType: String,
        val preview: Uri,
    ) : MediaFile()

    data class Video(
        override val original: Uri,
        override val mimeType: String,
        val preview: Uri,
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
