package noctiluca.features.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import noctiluca.features.navigation.utils.Serializable

data class AttachmentPreview(
    val params: List<AttachmentParams>,
    val index: Int,
) : ScreenProvider

data class AttachmentParams(
    val type: String,
    val url: String,
) : Serializable {
    enum class Type { IMAGE, VIDEO }
}
