package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator

sealed class TimelineScreen : ScreenProvider {
    data object Timelines : TimelineScreen()
    data object Toot : TimelineScreen()
}

fun Navigator.navigateToTimelines() {
    push(ScreenRegistry.get(TimelineScreen.Timelines))
}

@Composable
fun navigateToTimelines() {
    val navigator = LocalNavigator.current
    val timelines = rememberScreen(TimelineScreen.Timelines)

    navigator?.replaceAll(timelines)
}
