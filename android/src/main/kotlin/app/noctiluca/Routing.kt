package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import app.noctiluca.navigation.*

@Composable
fun Routing(
    navController: AndroidNavigationController,
    startDestination: String = RouteTimeline,
) = NavHost(navController.navHostController, startDestination = startDestination) {
    signIn(navController)
    timeline(navController)
    accountDetail(navController)
}
