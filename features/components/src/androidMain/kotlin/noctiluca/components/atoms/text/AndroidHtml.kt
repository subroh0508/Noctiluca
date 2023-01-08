package noctiluca.components.atoms.text

import android.text.Html
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun HtmlText(
    text: String,
    modifier: Modifier,
) {
    val spanned = remember(text) { Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT) }

    AndroidView(
        factory = { context -> TextView(context) },
        modifier = modifier,
        update = { view -> view.text = spanned },
    )
}
