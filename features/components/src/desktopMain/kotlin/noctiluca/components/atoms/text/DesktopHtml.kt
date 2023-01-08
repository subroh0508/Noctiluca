package noctiluca.components.atoms.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.text.TextStyle
import javax.swing.JEditorPane

@Composable
internal actual fun ExpectHtmlText(
    text: String,
    modifier: Modifier,
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
