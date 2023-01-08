package noctiluca.components.atoms.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import javax.swing.JEditorPane

@Composable
actual fun HtmlText(
    text: String,
    modifier: Modifier,
) {
    val htmlComponent = remember(text) {
        JEditorPane(text).apply {
            isEditable = false
        }
    }

    SwingPanel(
        factory = { htmlComponent },
        modifier = modifier,
    )
}
