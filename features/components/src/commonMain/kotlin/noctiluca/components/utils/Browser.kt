package noctiluca.components.utils

import androidx.compose.runtime.Composable
import noctiluca.model.Uri

@Composable
expect fun openBrowser(uri: Uri)
