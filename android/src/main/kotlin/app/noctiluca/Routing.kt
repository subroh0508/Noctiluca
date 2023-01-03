package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.*
import noctiluca.components.utils.Browser
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeCode
import noctiluca.features.timeline.di.TimelineModule

@Composable
fun Routing(
    authorizeCode: AuthorizeCode?,
    browser: Browser,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteSignIn,
) {
    NavHost(navController, startDestination = startDestination) {
        signIn(
            authorizeCode,
            SignInComponent(browser),
            onNavigateToTimeline = { navController.navigateToTimeline() },
        )

        timeline(
            TimelineModule(),
            onReload = { navController.reload() },
            onBackToSignIn = { navController.backToSignIn() },
        )
    }
}
