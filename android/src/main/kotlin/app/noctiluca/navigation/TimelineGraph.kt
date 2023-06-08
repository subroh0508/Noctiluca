package app.noctiluca.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.timeline.TimelineNavigation
import noctiluca.features.timeline.TimelineScreen
import noctiluca.features.timeline.di.TimelineComponent

const val RouteTimeline = "Timeline"

fun NavGraphBuilder.timeline(
    navController: TimelineNavigation,
) {
    composable(RouteTimeline) {
        TimelineScreen(
            TimelineComponent(),
            navController,
        )
    }
}
