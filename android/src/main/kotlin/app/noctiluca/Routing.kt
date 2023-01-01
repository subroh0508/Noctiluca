package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import noctiluca.features.authentication.RequestAccessToken
import noctiluca.features.authentication.SignIn

@Composable
fun Routing(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "signin",
) {
    val deepLinkOfFetchAccessToken = buildDeepLinkOfFetchAccessToken()

    NavHost(navController, startDestination = startDestination, Modifier) {
        composable("signin") { SignIn() }
        composable(
            "fetch_access_token",
            deepLinks = listOf(navDeepLink { uriPattern = deepLinkOfFetchAccessToken }),
        ) { RequestAccessToken(it) }
    }
}

@Composable
private fun buildDeepLinkOfFetchAccessToken() = buildString {
    append(
        stringResource(R.string.sign_in_oauth_scheme),
        "://",
        stringResource(R.string.sign_in_client_name),
        "?code={code}",
    )
}
