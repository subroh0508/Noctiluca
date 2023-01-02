package noctiluca.features.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.intl.Locale
import androidx.navigation.NavBackStackEntry
import noctiluca.components.FeatureComponent
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.state.rememberAuthorizedUser
import org.koin.core.component.KoinScopeComponent

@Composable
fun RequestAccessToken(
    code: String?,
    koinComponent: KoinScopeComponent,
) = FeatureComponent(koinComponent) {
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides koinComponent.scope,
    ) {
        println("code: $code")
        val authorizedUser by rememberAuthorizedUser(code)

        println(authorizedUser?.id)
        println(authorizedUser?.hostname)
    }
}
