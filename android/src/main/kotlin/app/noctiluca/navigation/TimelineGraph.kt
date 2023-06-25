package app.noctiluca.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.timeline.TimelineNavigation
import noctiluca.features.timeline.TimelinesScreen
import noctiluca.features.timeline.di.TimelineComponent

const val RouteTimelines = "Timelines"
const val RouteToot = "Toot"

fun NavGraphBuilder.timeline(
    navigation: TimelineNavigation,
) = composable(RouteTimelines) {
    TimelinesScreen(
        TimelineComponent(),
        navigation,
    )
}
