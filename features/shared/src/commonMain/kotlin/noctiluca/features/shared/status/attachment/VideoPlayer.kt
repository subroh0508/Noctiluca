package noctiluca.features.shared.status.attachment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.model.Uri

@Composable
expect fun VideoPlayer(
    url: Uri,
    modifier: Modifier = Modifier,
)
