package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.features.authentication.LocalContext
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.UnknownException
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.instance.model.Instance
import noctiluca.model.Domain
import noctiluca.model.Uri

class AuthorizationViewModel(
    private val clientName: String,
    private val redirectUri: Uri,
    private val navigation: SignInNavigation?,
    private val requestAppCredentialUseCase: RequestAppCredentialUseCase,
    private val requestRequestAccessTokenUseCase: RequestAccessTokenUseCase,
    coroutineScope: CoroutineScope,
    lifecycleRegistry: LifecycleRegistry,
    rootContext: ComponentContext,
) : ViewModel(coroutineScope, lifecycleRegistry, rootContext) {
    private val authorizationLoadState by lazy { MutableValue<LoadState>(LoadState.Initial) }

    fun requestAuthorize(instance: Instance) {
        val domain = Domain(instance.domain)

        val job = launchLazy {
            runCatching { requestAppCredentialUseCase.execute(domain, clientName, redirectUri) }
                .onSuccess { /*navigation?.openBrowser(it)*/ }
                .onFailure { authorizationLoadState.value = LoadState.Error(it) }
        }

        authorizationLoadState.value = LoadState.Loading(job)
        job.start()
    }

    fun fetchAccessToken(result: AuthorizeResult?) {
        val code = result?.getCodeOrNull()
        val error = result?.getErrorOrNull()

        if (authorizationLoadState.value !is LoadState.Loading) {
            return
        }

        if (code == null) {
            authorizationLoadState.value = error?.let(LoadState::Error) ?: LoadState.Initial
            return
        }

        val job = launchLazy {
            runCatching { requestRequestAccessTokenUseCase.execute(code, redirectUri) }
                .onSuccess {
                    if (it != null) {
                        navigation?.navigateToTimelines()
                        return@onSuccess
                    }

                    authorizationLoadState.value = LoadState.Error(UnknownException)
                }
                .onFailure { authorizationLoadState.value = LoadState.Error(it) }
        }

        authorizationLoadState.value = LoadState.Loading(job)
        job.start()
    }

    companion object Factory {
        @Composable
        operator fun invoke(
            domain: String,
            lifecycleRegistry: LifecycleRegistry,
            context: ComponentContext,
        ): AuthorizationViewModel {
            val koinScope = LocalScope.current
            val navigation = LocalContext.current

            return AuthorizationViewModel(
                getString().sign_in_client_name,
                buildRedirectUri(domain),
                null, /*navigation*/
                remember { koinScope.get() },
                remember { koinScope.get() },
                rememberCoroutineScope(),
                lifecycleRegistry,
                context.childContext(
                    "Authorization",
                    lifecycleRegistry,
                ),
            )
        }
    }
}
