package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator

sealed class Timeline : ScreenProvider {
    data object TimelineLane : Timeline()
    data object Toot : Timeline()
}

@Composable
fun navigateToTimelines() {
    val navigator = LocalNavigator.current
    val timelines = rememberScreen(Timeline.TimelineLane)

    navigator?.replaceAll(timelines)
}
