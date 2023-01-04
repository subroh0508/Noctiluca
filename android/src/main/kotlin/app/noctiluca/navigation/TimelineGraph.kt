package app.noctiluca.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.timeline.TimelineScreen
import noctiluca.features.timeline.di.TimelineComponent
import org.koin.core.component.KoinScopeComponent

const val RouteTimeline = "Timeline"

fun NavGraphBuilder.timeline(
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) {
    composable(RouteTimeline) {
        TimelineScreen(TimelineComponent(), onReload, onBackToSignIn)
    }
}

fun NavController.reload() {
    navigate(RouteTimeline) {
        popUpTo(RouteTimeline) { inclusive = true }
    }
}

fun NavController.backToSignIn() {
    navigate(RouteSignIn) {
        popUpTo(RouteTimeline) { inclusive = true }
    }
}
