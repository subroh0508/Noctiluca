package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import noctiluca.features.authentication.LocalNavigator
import noctiluca.features.authentication.SignInNavigator
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.authentication.viewmodel.instancedetail.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.instancedetail.ShowMastodonInstanceDetailViewModel
import noctiluca.features.components.ViewModel
import noctiluca.model.Uri

class MastodonInstanceDetailViewModel private constructor(
    val domain: String,
    clientName: String,
    redirectUri: Uri,
    navigator: SignInNavigator?,
    coroutineScope: CoroutineScope,
    context: SignInNavigator.Screen,
) : ViewModel(coroutineScope),
    AuthorizeViewModel by AuthorizeViewModel(
        clientName,
        redirectUri,
        navigator,
        coroutineScope,
        context,
    ),
    ShowMastodonInstanceDetailViewModel by ShowMastodonInstanceDetailViewModel(
        domain,
        coroutineScope,
        context,
    ) {
    companion object Provider {
        @Composable
        operator fun invoke(
            domain: String,
            context: SignInNavigator.Screen,
        ): MastodonInstanceDetailViewModel {
            val clientName = getString().sign_in_client_name
            val redirectUri = buildRedirectUri(domain)
            val navigator = LocalNavigator.current
            val coroutineScope = rememberCoroutineScope()

            return remember {
                MastodonInstanceDetailViewModel(
                    domain,
                    clientName,
                    redirectUri,
                    navigator,
                    coroutineScope,
                    context,
                )
            }
        }
    }
}
