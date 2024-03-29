import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import app.noctiluca.shared.AppEntryPoint
import app.noctiluca.shared.MainView
import io.ktor.client.engine.darwin.Darwin
import noctiluca.features.signin.HandleDeepLink
import org.koin.core.KoinApplication

private val koinApp = compositionLocalOf<KoinApplication?> { null }
private val httpClientEngine = Darwin.create()

fun handleDeepLink(
    host: String?,
    query: String?,
) = noctiluca.features.signin.handleDeepLink(host, query)

@Suppress("FunctionName")
fun MainViewController() = ComposeUIViewController {
    CompositionLocalProvider(
        koinApp provides AppEntryPoint.init(
            httpClientEngine,
            buildImageLoader(httpClientEngine),
        )
    ) {
        HandleDeepLink()
        MainView()
    }
}
