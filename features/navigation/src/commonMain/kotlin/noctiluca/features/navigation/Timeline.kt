package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator

sealed class Timeline : ScreenProvider {
    data object TimelineLane : Timeline()
    data object Toot : Timeline()
}

fun Navigator.navigateToTimelines() {
    push(ScreenRegistry.get(Timeline.TimelineLane))
}

@Composable
fun navigateToTimelines() {
    val navigator = LocalNavigator.current
    val timelines = rememberScreen(Timeline.TimelineLane)

    navigator?.replaceAll(timelines)
}
