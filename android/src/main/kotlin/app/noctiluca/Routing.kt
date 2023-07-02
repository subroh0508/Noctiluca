package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import app.noctiluca.navigation.*
import com.arkivanov.decompose.ComponentContext

@Composable
fun Routing(
    rootComponent: ComponentContext,
    navigation: AndroidNavigation,
    startDestination: String = RouteTimeline,
) = NavHost(
    navigation.navHostController,
    startDestination = startDestination,
) {
    signIn(rootComponent, navigation)
    timeline(navigation)
    accountDetail(navigation)
}
