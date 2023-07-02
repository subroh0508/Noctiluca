package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
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
    coroutineScope: CoroutineScope,
    lifecycleRegistry: LifecycleRegistry,
    context: SignInNavigator.Child.MastodonInstanceDetail,
) : ViewModel(
    coroutineScope,
    lifecycleRegistry,
    context,
),
    AuthorizeViewModel by AuthorizeViewModel(
        clientName,
        redirectUri,
        coroutineScope,
        lifecycleRegistry,
        context,
    ),
    ShowMastodonInstanceDetailViewModel by ShowMastodonInstanceDetailViewModel(
        domain,
        coroutineScope,
        lifecycleRegistry,
        context,
    ) {
    companion object Provider {
        @Composable
        operator fun invoke(
            domain: String,
            context: SignInNavigator.Child.MastodonInstanceDetail,
        ): MastodonInstanceDetailViewModel {
            val clientName = getString().sign_in_client_name
            val redirectUri = buildRedirectUri(domain)
            val coroutineScope = rememberCoroutineScope()
            val lifecycleRegistry = remember { LifecycleRegistry() }

            return remember {
                MastodonInstanceDetailViewModel(
                    domain,
                    clientName,
                    redirectUri,
                    coroutineScope,
                    lifecycleRegistry,
                    context,
                )
            }
        }
    }
}
