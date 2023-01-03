package app.noctiluca.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.timeline.TimelineScreen
import org.koin.core.component.KoinScopeComponent

const val RouteTimeline = "timeline"

fun NavGraphBuilder.timeline(
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) {
    composable(RouteTimeline) {
        TimelineScreen(component, onReload, onBackToSignIn)
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
