package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import app.noctiluca.navigation.*

@Composable
fun Routing(
    navigation: AndroidNavigation,
    startDestination: String = RouteTimeline,
) = NavHost(navigation.navHostController, startDestination = startDestination) {
    signIn(navigation)
    timeline(navigation)
    accountDetail(navigation)
}
