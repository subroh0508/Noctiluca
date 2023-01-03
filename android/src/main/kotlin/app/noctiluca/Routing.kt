package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.*
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.model.AuthorizeCode
import noctiluca.features.timeline.di.TimelineModule

@Composable
fun Routing(
    authorizeState: State<AuthorizeCode?>,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "signin",
) {
    NavHost(navController, startDestination = startDestination, Modifier) {
        signIn(
            authorizeState,
            SignInModule(),
            onNavigateToTimeline = { navController.navigateToTimeline() },
        )

        timeline(
            TimelineModule(),
            onReload = { navController.reload() },
            onBackToSignIn = { navController.backToSignIn() },
        )
    }
}