package noctiluca.features.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.shared.LocalAuthorizedContext
import noctiluca.model.AuthorizeEventState

data object SplashScreen : Screen {
    @Composable
    override fun Content() = AuthorizedComposable {
        val context = LocalAuthorizedContext.current
        val state by context.state.collectAsState(AuthorizeEventState())

        if (state.event == AuthorizeEventState.Event.OK) {
            navigateToTimelines(state.user)
        }
    }
}
