package noctiluca.features.attachment.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import noctiluca.features.attachment.AttachmentPreviewScreen
import noctiluca.features.attachment.section.AttachmentPreviewContent
import noctiluca.features.attachment.section.AttachmentPreviewTopAppBar
import noctiluca.features.navigation.AttachmentParams
import noctiluca.model.Uri

@Composable
internal fun AttachmentPreviewScreen.AttachmentPreviewScaffold() {
    var isVisibleTopAppBar by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { AttachmentPreviewTopAppBar(isVisibleTopAppBar) },
    ) {
        AttachmentPreviewContent(
            params.map { AttachmentParams.Type.valueOf(it.type) to Uri(it.url) },
            index,
            isVisibleTopAppBar,
            onToggleTopAppBar = { visibility -> isVisibleTopAppBar = visibility },
        )
    }
}
