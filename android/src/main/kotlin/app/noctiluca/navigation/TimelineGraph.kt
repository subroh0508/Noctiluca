package app.noctiluca.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import noctiluca.features.timeline.TimelineNavigation
import noctiluca.features.timeline.TimelinesScreen
import noctiluca.features.timeline.TootScreen
import noctiluca.features.timeline.di.TimelineComponent

const val ComposableTimelines = "Timelines"
const val ComposableToot = "Toot"

const val RouteTimeline = "Timeline"

fun NavGraphBuilder.timeline(
    navigation: TimelineNavigation,
) = navigation(
    startDestination = ComposableTimelines,
    route = RouteTimeline,
) {
    composable(ComposableTimelines) {
        TimelinesScreen(
            TimelineComponent(),
            navigation,
        )
    }

    composable(ComposableToot) {
        TootScreen(
            TimelineComponent(),
            navigation,
        )
    }
}
