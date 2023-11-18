package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator

sealed class SignInScreen : ScreenProvider {
    data object MastodonInstanceList : SignInScreen()
    data class MastodonInstanceDetail(
        val domain: String,
        val query: String?,
    ) : SignInScreen()
}

fun Navigator.navigateToSignIn() {
    push(ScreenRegistry.get(SignInScreen.MastodonInstanceList))
}

@Composable
fun backToSignIn() {
    val navigator = LocalNavigator.current
    val signIn = rememberScreen(SignInScreen.MastodonInstanceList)

    navigator?.replaceAll(signIn)
}

@Composable
fun navigateToSignIn() {
    val navigator = LocalNavigator.current
    val signIn = rememberScreen(SignInScreen.MastodonInstanceList)

    navigator?.push(signIn)
}
