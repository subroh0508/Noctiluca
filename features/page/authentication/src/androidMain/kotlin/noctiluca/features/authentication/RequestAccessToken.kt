package noctiluca.features.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import noctiluca.features.authentication.state.rememberAuthorizedUser

@Composable
fun RequestAccessToken(
    backStackEntry: NavBackStackEntry,
) {
    val authorizedUser by rememberAuthorizedUser(backStackEntry.arguments?.getString("code"))

    println(authorizedUser?.id)
    println(authorizedUser?.hostname)
}
