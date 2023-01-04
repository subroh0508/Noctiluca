package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.*
import noctiluca.components.utils.Browser
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.timeline.di.TimelineComponent

@Composable
fun Routing(
    authorizeResult: AuthorizeResult?,
    browser: Browser,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteTimeline,
) {
    NavHost(navController, startDestination = startDestination) {
        signIn(
            authorizeResult,
            SignInComponent(browser),
            onNavigateToTimeline = { navController.navigateToTimeline() },
        )

        timeline(
            TimelineComponent(),
            onReload = { navController.reload() },
            onBackToSignIn = { navController.backToSignIn() },
        )
    }
}
