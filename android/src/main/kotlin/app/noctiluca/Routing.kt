package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import app.noctiluca.navigation.*
import noctiluca.features.components.utils.Browser

@Composable
fun Routing(
    browser: Browser,
    navController: NavHostController,
    startDestination: String = RouteTimeline,
) = NavHost(navController, startDestination = startDestination) {
    signIn(
        browser,
        onNavigateToTimeline = { navController.navigateToTimeline() },
    )

    timeline(
        onReload = { navController.reload() },
        onBackToSignIn = { navController.backToSignIn() },
    )
}
