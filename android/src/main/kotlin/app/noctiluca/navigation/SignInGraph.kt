package app.noctiluca.navigation

import android.net.Uri
import androidx.navigation.*
import androidx.navigation.compose.composable
import noctiluca.features.authentication.InstanceDetailScreen
import noctiluca.features.authentication.SearchInstanceSuggestsScreen
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.invoke
import noctiluca.features.components.utils.Browser

const val ComposableSearchInstance = "SearchInstance"
const val ComposableInstanceDetail = "InstanceDetail"

const val RouteSignIn = "SignIn"

fun NavGraphBuilder.signIn(
    browser: Browser,
    onNavigateToTimeline: () -> Unit,
    onNavigateToInstanceDetail: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    navigation(
        startDestination = ComposableSearchInstance,
        route = RouteSignIn,
    ) {
        composable(ComposableSearchInstance) {
            SearchInstanceSuggestsScreen(
                SignInComponent(browser),
                onNavigateToInstanceDetail,
            )
        }
        composable("$ComposableInstanceDetail?${AuthorizeResult.Query}") { navBackStackEntry ->
            val domain = navBackStackEntry.arguments?.getString(AuthorizeResult.QUERY_DOMAIN)
            val result = navBackStackEntry.arguments?.let { AuthorizeResult(it) }

            InstanceDetailScreen(
                domain,
                result,
                SignInComponent(browser),
                onNavigateToTimeline,
                onBackPressed,
            )
        }
    }
}

fun NavController.navigateToTimeline() {
    navigate(RouteTimeline) {
        popUpTo(RouteSignIn) { inclusive = true }
    }
}

fun NavController.navigateToInstanceDetail(domain: String) {
    navigate("$ComposableInstanceDetail?${AuthorizeResult.QUERY_DOMAIN}=$domain")
}

fun NavController.redirectToSignIn(uri: Uri?) {
    uri ?: return

    navigate(
        buildString {
            append("$ComposableInstanceDetail?")
            append("${AuthorizeResult.QUERY_DOMAIN}=${uri.host}&")
            append(uri.query)
        },
    ) {
        launchSingleTop = true
    }
}
