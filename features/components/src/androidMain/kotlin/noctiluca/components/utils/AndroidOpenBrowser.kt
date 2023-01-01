package noctiluca.components.utils

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.net.Uri as AndroidUri
import noctiluca.model.Uri

@Composable
actual fun openBrowser(uri: Uri) = LocalContext.current.startActivity(Intent(Intent.ACTION_VIEW, AndroidUri.parse(uri.value)))
