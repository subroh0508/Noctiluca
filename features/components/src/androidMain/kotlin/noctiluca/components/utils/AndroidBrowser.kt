package noctiluca.components.utils

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.net.Uri as AndroidUri
import noctiluca.model.Uri

@Composable
actual fun openBrowser(uri: Uri) = CustomTabsIntent.Builder()
    .setShowTitle(true)
    .build()
    .launchUrl(LocalContext.current, AndroidUri.parse(uri.value))
