package noctiluca.features.shared.atoms.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import javax.swing.JEditorPane

@Composable
internal actual fun ExpectHtmlText(
    text: String,
    modifier: Modifier,
    urlColor: Color,
    color: Color,
    overflow: TextOverflow,
    style: TextStyle,
) {
    val htmlComponent = remember(text) {
        JEditorPane("text/html", text).apply {
            isEditable = false
        }
    }

    SwingPanel(
        factory = { htmlComponent },
        modifier = modifier,
    )
}
