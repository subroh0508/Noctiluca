package app.noctiluca.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.invoke

const val RouteSignIn = "SignIn"

fun NavGraphBuilder.signIn(
    navigation: SignInNavigation,
) = composable("$RouteSignIn?${AuthorizeResult.Query}") { navBackStackEntry ->
    val domain = navBackStackEntry.arguments?.getString(AuthorizeResult.QUERY_DOMAIN)
    val result = navBackStackEntry.arguments?.let { AuthorizeResult(it) }

    SignInScreen(
        domain,
        result,
        navigation,
    )
}
