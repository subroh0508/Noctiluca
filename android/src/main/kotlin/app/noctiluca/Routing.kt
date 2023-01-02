package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import noctiluca.features.authentication.SignIn
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.model.AuthorizeCode

@Composable
fun Routing(
    authorizeState: State<AuthorizeCode?>,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "signin",
) {
    NavHost(navController, startDestination = startDestination, Modifier) {
        composable("signin") { SignIn(authorizeState, SignInModule()) }
    }
}
