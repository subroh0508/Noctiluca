package noctiluca.features.shared.utils

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import noctiluca.model.Uri
import android.net.Uri as AndroidUri

actual class Browser(
    private val context: Context,
) {
    actual fun open(uri: Uri) = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
        .launchUrl(context, AndroidUri.parse(uri.value))
}

@Composable
actual fun openBrowser(uri: Uri) = CustomTabsIntent.Builder()
    .setShowTitle(true)
    .build()
    .launchUrl(LocalContext.current, AndroidUri.parse(uri.value))
