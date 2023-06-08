package app.noctiluca.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import noctiluca.features.authentication.InstanceDetailScreen
import noctiluca.features.authentication.SearchInstanceSuggestsScreen
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.invoke

const val ComposableSearchInstance = "SearchInstance"
const val ComposableInstanceDetail = "InstanceDetail"

const val RouteSignIn = "SignIn"

fun NavGraphBuilder.signIn(
    navController: SignInNavigation,
) = navigation(
    startDestination = ComposableSearchInstance,
    route = RouteSignIn,
) {
    composable(ComposableSearchInstance) {
        SearchInstanceSuggestsScreen(
            SignInComponent(),
            navController,
        )
    }
    composable("$ComposableInstanceDetail?${AuthorizeResult.Query}") { navBackStackEntry ->
        val domain = navBackStackEntry.arguments?.getString(AuthorizeResult.QUERY_DOMAIN)
        val result = navBackStackEntry.arguments?.let { AuthorizeResult(it) }

        InstanceDetailScreen(
            domain,
            result,
            SignInComponent(),
            navController,
        )
    }
}
