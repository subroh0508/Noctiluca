package noctiluca.features.components.utils

import androidx.compose.runtime.Composable
import noctiluca.model.Uri

expect class Browser {
    fun open(uri: Uri)
}

@Composable
expect fun openBrowser(uri: Uri)
