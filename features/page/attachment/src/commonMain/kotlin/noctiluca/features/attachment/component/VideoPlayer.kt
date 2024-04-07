package noctiluca.features.attachment.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.model.Uri

@Composable
expect fun VideoPlayer(
    url: Uri,
    isControllerVisible: Boolean,
    onChangeControllerVisibility: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
)
