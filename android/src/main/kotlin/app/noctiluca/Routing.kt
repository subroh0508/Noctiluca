package app.noctiluca

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.noctiluca.extensions.uri
import noctiluca.features.authentication.RequestAccessToken
import noctiluca.features.authentication.SignIn
import noctiluca.features.authentication.di.SignInModule

@Composable
fun Routing(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "signin",
) {
    val deepLinkOfFetchAccessToken = buildDeepLinkOfFetchAccessToken()

    NavHost(navController, startDestination = startDestination, Modifier) {
        composable("signin") { SignIn(SignInModule()) }
        composable(
            "fetch_access_token?code={code}",
            deepLinks = listOf(navDeepLink {
                action = Intent.ACTION_VIEW
                uriPattern = deepLinkOfFetchAccessToken
            }),
        ) {
            RequestAccessToken(
                it.uri?.getQueryParameter("code"),
                SignInModule(),
            )
        }
    }
}

@Composable
private fun buildDeepLinkOfFetchAccessToken() = buildString {
    append(
        stringResource(R.string.sign_in_oauth_scheme),
        "://",
        stringResource(R.string.sign_in_client_name),
    )
}
