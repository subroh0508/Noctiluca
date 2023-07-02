package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.authentication.viewmodel.context.SignInFeatureContext
import noctiluca.features.authentication.viewmodel.instancedetail.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.instancedetail.ShowMastodonInstanceDetailViewModel
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.status.model.Status

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
), AuthorizeViewModel by AuthorizeViewModel(
    clientName,
    redirectUri,
    navigation,
    coroutineScope,
    lifecycleRegistry,
    context,
), ShowMastodonInstanceDetailViewModel by ShowMastodonInstanceDetailViewModel(
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
            lifecycleRegistry: LifecycleRegistry,
            context: SignInFeatureContext.Child.MastodonInstanceDetail,
        ): MastodonInstanceDetailViewModel {
            val clientName = getString().sign_in_client_name
            val redirectUri = buildRedirectUri(domain)
            val coroutineScope = rememberCoroutineScope()

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
