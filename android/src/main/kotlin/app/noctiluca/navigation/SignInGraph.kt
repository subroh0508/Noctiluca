package app.noctiluca.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import noctiluca.features.authentication.InstanceDetailScreen
import noctiluca.features.authentication.SearchInstanceSuggestsScreen
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.invoke
import noctiluca.features.components.utils.Browser

const val ComposableSearchInstance = "SearchInstance"
const val ComposableInstanceDetail = "InstanceDetail"

const val RouteSignIn = "SignIn"

fun NavGraphBuilder.signIn(
    browser: Browser,
    navController: SignInNavigation,
) {
    navigation(
        startDestination = ComposableSearchInstance,
        route = RouteSignIn,
    ) {
        composable(ComposableSearchInstance) {
            SearchInstanceSuggestsScreen(
                SignInComponent(browser),
                navController,
            )
        }
        composable("$ComposableInstanceDetail?${AuthorizeResult.Query}") { navBackStackEntry ->
            val domain = navBackStackEntry.arguments?.getString(AuthorizeResult.QUERY_DOMAIN)
            val result = navBackStackEntry.arguments?.let { AuthorizeResult(it) }

            InstanceDetailScreen(
                domain,
                result,
                SignInComponent(browser),
                navController,
            )
        }
    }
}
