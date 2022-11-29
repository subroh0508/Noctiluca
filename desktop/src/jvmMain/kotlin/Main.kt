import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.subroh0508.common.App


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
