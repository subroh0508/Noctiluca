package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.authentication.viewmodel.context.SignInFeatureContext
import noctiluca.features.authentication.viewmodel.instancedetail.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.instancedetail.ShowMastodonInstanceDetailViewModel
import noctiluca.features.components.ViewModel
import noctiluca.model.Uri

class MastodonInstanceDetailViewModel private constructor(
    val domain: String,
    clientName: String,
    redirectUri: Uri,
    navigation: SignInNavigation?,
    coroutineScope: CoroutineScope,
    lifecycleRegistry: LifecycleRegistry,
    context: SignInFeatureContext.Child.MastodonInstanceDetail,
) : ViewModel(
    coroutineScope,
    lifecycleRegistry,
    context,
),
    AuthorizeViewModel by AuthorizeViewModel(
        clientName,
        redirectUri,
        navigation,
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
            navigation: SignInNavigation?,
            context: SignInFeatureContext.Child.MastodonInstanceDetail,
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
                    navigation,
                    coroutineScope,
                    lifecycleRegistry,
                    context,
                )
            }
        }
    }
}
