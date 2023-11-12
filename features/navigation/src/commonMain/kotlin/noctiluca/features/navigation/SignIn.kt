package noctiluca.features.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SignInScreen : ScreenProvider {
    data object MastodonInstanceList : SignInScreen()
    data class MastodonInstanceDetail(
        val domain: String,
        val query: String?,
    ) : SignInScreen()
}
