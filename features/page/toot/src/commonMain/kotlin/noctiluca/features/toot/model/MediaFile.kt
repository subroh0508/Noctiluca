package noctiluca.features.toot.model

import noctiluca.model.Uri

sealed class MediaFile {
    abstract val url: Uri
    abstract val mimeType: String

    data class Image(
        override val url: Uri,
        override val mimeType: String,
    ) : MediaFile()

    data class Video(
        override val url: Uri,
        override val mimeType: String,
    ) : MediaFile()

    data class Audio(
        override val url: Uri,
        override val mimeType: String,
    ) : MediaFile()

    data class Unknown(
        override val url: Uri,
        override val mimeType: String,
    ) : MediaFile()
}
