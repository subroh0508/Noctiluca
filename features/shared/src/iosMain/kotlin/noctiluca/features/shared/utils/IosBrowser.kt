package noctiluca.features.shared.utils

import androidx.compose.runtime.Composable
import noctiluca.model.Uri
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class Browser {
    actual fun open(uri: Uri) = Unit
}

@Composable
actual fun openBrowser(uri: Uri) {
    val nsUrl = NSURL.URLWithString(uri.value) ?: return

    UIApplication.sharedApplication.openURL(nsUrl)
}
