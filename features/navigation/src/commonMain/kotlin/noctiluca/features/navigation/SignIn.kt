package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import noctiluca.features.navigation.utils.Serializable

data class SignIn(val params: SignInParams) : ScreenProvider

sealed class SignInParams : Serializable

data object MastodonInstanceListParams : SignInParams()
data class MastodonInstanceDetailParams(
    val domain: String,
    val query: String?,
) : SignInParams()

@Composable
fun rememberSignInScreen() = rememberScreen((SignIn(MastodonInstanceListParams)))

fun Navigator.navigateToSignIn() {
    push(ScreenRegistry.get(SignIn(MastodonInstanceListParams)))
}

@Composable
fun backToSignIn() {
    val navigator = LocalNavigator.current
    val signIn = rememberScreen(SignIn(MastodonInstanceListParams))

    navigator?.replaceAll(signIn)
}
