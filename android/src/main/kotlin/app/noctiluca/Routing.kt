package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import app.noctiluca.navigation.*
import noctiluca.features.components.utils.Browser

@Composable
fun Routing(
    browser: Browser,
    navController: AndroidNavigationController,
    startDestination: String = RouteTimeline,
) = NavHost(navController.navHostController, startDestination = startDestination) {
    signIn(browser, navController)
    timeline(navController)
    accountDetail(navController)
}
