package noctiluca.components.utils

import androidx.compose.runtime.Composable
import noctiluca.model.Uri

actual class Browser {
    actual fun open(uri: Uri) = Unit
}

@Composable
actual fun openBrowser(uri: Uri) = Unit
