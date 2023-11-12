package noctiluca.features.navigation

import android.net.Uri
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator

fun Navigator.redirectToSignIn(uri: Uri) {
    val host = uri.host ?: return

    val screen = ScreenRegistry.get(
        SignInScreen.MastodonInstanceDetail(host, uri.query),
    )

    push(screen)
}
