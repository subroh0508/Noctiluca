package noctiluca.model.media

import noctiluca.model.Uri

sealed class LocalMediaFile {
    abstract val original: Uri
    abstract val mimeType: String

    data class Image(
        override val original: Uri,
        override val mimeType: String,
    ) : LocalMediaFile()

    data class Video(
        override val original: Uri,
        override val mimeType: String,
    ) : LocalMediaFile()

    data class Audio(
        override val original: Uri,
        override val mimeType: String,
    ) : LocalMediaFile()

    data class Unknown(
        override val original: Uri,
        override val mimeType: String,
    ) : LocalMediaFile()
}
