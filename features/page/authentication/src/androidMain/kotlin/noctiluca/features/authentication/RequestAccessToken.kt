package noctiluca.features.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import noctiluca.components.FeatureComponent
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.state.rememberAuthorizedUser

@Composable
fun RequestAccessToken(
    code: String?,
) = FeatureComponent(SignInModule) {
    println("code: $code")
    val authorizedUser by rememberAuthorizedUser(code)

    println(authorizedUser?.id)
    println(authorizedUser?.hostname)
}
