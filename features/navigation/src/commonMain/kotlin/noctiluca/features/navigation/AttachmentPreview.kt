package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.rememberScreen
import noctiluca.features.navigation.utils.Serializable
import noctiluca.model.status.Attachment

data class AttachmentPreview(
    val params: List<AttachmentParams>,
    val index: Int,
) : ScreenProvider

data class AttachmentParams(
    val type: String,
    val url: String,
) : Serializable {
    enum class Type { IMAGE, VIDEO }

    internal constructor(attachment: Attachment) : this(
        when (attachment) {
            is Attachment.Video -> Type.VIDEO
            is Attachment.Image, is Attachment.Gifv -> Type.IMAGE
            else -> error("Illegal attachment type: ${attachment::class.simpleName}")
        }.name,
        attachment.url.value,
    )
}

@Composable
fun rememberAttachmentPreview(
    attachments: List<Attachment>,
    index: Int,
) = rememberScreen(AttachmentPreview(attachments.map { AttachmentParams(it) }, index))
