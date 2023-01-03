package app.noctiluca

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.*
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.model.AuthorizeCode
import noctiluca.features.timeline.TimelineScreen
import noctiluca.features.timeline.di.TimelineModule

@Composable
fun Routing(
    authorizeCode: AuthorizeCode?,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteSignIn,
) {
    NavHost(navController, startDestination = startDestination) {
        signIn(
            authorizeCode,
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
