package app.noctiluca.navigation

import android.net.Uri
import androidx.navigation.*
import androidx.navigation.compose.composable
import noctiluca.components.utils.Browser
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.invoke

const val RouteSignIn = "SignIn"

fun NavGraphBuilder.signIn(
    browser: Browser,
    onNavigateToTimeline: () -> Unit,
) {
    composable("$RouteSignIn?${AuthorizeResult.Query}") { navBackStackEntry ->
        val result = navBackStackEntry.arguments?.let { AuthorizeResult(it) }

        SignInScreen(result, SignInComponent(browser), onNavigateToTimeline)
    }
}

fun NavController.navigateToTimeline() {
    navigate(RouteTimeline) {
        popUpTo(RouteSignIn) { inclusive = true }
    }
}

fun NavController.redirectToSignIn(uri: Uri?) {
    uri ?: return

    navigate("$RouteSignIn?${uri.query}") { launchSingleTop = true }
}
