import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import noctiluca.features.timeline.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
