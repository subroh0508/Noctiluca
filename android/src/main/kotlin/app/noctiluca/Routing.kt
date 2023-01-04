package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.*
import noctiluca.components.utils.Browser
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.timeline.di.TimelineComponent

@Composable
fun Routing(
    browser: Browser,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteTimeline,
) {
    val redirectUri = buildRedirectUri()

    NavHost(navController, startDestination = startDestination) {
        signIn(
            browser,
            onNavigateToTimeline = { navController.navigateToTimeline() },
        )

        timeline(
            onReload = { navController.reload() },
            onBackToSignIn = { navController.backToSignIn() },
        )
    }
}
