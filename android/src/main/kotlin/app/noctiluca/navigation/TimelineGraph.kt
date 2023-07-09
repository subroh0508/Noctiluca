package app.noctiluca.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.timeline.TimelineNavigator
import noctiluca.features.timeline.TimelineScreen

const val ComposableToot = "Toot"

const val RouteTimeline = "Timeline"

fun NavGraphBuilder.timeline(
    navigator: TimelineNavigator,
) = composable(RouteTimeline) {
    TimelineScreen(TimelineNavigator.Screen(navigator))
}
