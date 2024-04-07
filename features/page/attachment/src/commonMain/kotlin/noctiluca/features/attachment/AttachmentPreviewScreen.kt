package noctiluca.features.attachment

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import noctiluca.features.navigation.AttachmentParams
import noctiluca.features.navigation.AttachmentPreview
import noctiluca.features.shared.AuthorizedComposable

val featureAttachmentScreenModule = screenModule {
    register<AttachmentPreview> { (params, index) ->
        AttachmentPreviewScreen(params, index)
    }
}

data class AttachmentPreviewScreen(
    val params: List<AttachmentParams>,
    val index: Int,
) : Screen {
    override val key: ScreenKey by lazy { params.joinToString { it.url } }

    @Composable
    override fun Content() = AuthorizedComposable(

    ) {

    }
}
