package app.noctiluca.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.model.AuthorizeResult
import org.koin.core.component.KoinScopeComponent

const val RouteSignIn = "signIn"

fun NavGraphBuilder.signIn(
    authorizeResult: AuthorizeResult?,
    koinComponent: KoinScopeComponent,
    onNavigateToTimeline: () -> Unit,
) {
    composable(RouteSignIn) {
        SignInScreen(authorizeResult, koinComponent, onNavigateToTimeline)
    }
}

fun NavController.navigateToTimeline() {
    navigate(RouteTimeline) {
        popUpTo(RouteSignIn) { inclusive = true }
    }
}
