package noctiluca.features.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.features.navigation.MastodonInstanceDetailParams
import noctiluca.features.navigation.SignIn

private val deepLinkStateFlow = MutableStateFlow<MastodonInstanceDetailParams?>(null)

fun handleDeepLink(domain: String?, query: String?) {
    deepLinkStateFlow.value = domain?.let { MastodonInstanceDetailParams(it, query) }
}

@Composable
actual fun HandleDeepLink() {
    val params by deepLinkStateFlow.collectAsState()
    val screen = rememberScreen(params?.let { SignIn(it) } ?: return)

    val navigator = LocalNavigator.current

    navigator?.push(screen)
}
