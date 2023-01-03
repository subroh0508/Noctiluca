package app.noctiluca.navigation

import androidx.compose.runtime.State
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.model.AuthorizeCode
import org.koin.core.component.KoinScopeComponent

const val RouteSignIn = "signIn"

fun NavGraphBuilder.signIn(
    authorizeCode: AuthorizeCode?,
    koinComponent: KoinScopeComponent,
    onNavigateToTimeline: () -> Unit,
) {
    composable(RouteSignIn) {
        SignInScreen(authorizeCode, koinComponent, onNavigateToTimeline)
    }
}

fun NavController.navigateToTimeline() {
    navigate(RouteTimeline) /*{
        //popUpTo(RouteSignIn) { inclusive = true }
    }*/
}
