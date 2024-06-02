package noctiluca.model.status

import noctiluca.model.Uri

sealed class Attachment {
    abstract val id: String
    abstract val url: Uri
    abstract val previewUrl: Uri
    abstract val description: String?

    data class Image(
        override val id: String,
        override val url: Uri,
        override val previewUrl: Uri,
        override val description: String?,
    ) : Attachment()

    data class Gifv(
        override val id: String,
        override val url: Uri,
        override val previewUrl: Uri,
        override val description: String?,
    ) : Attachment()

    data class Video(
        override val id: String,
        override val url: Uri,
        override val previewUrl: Uri,
        override val description: String?,
    ) : Attachment()

    data class Audio(
        override val id: String,
        override val url: Uri,
        override val previewUrl: Uri,
        override val description: String?,
    ) : Attachment()

    data class Unknown(
        override val id: String,
        override val url: Uri,
        override val previewUrl: Uri,
        override val description: String?,
    ) : Attachment()
}
