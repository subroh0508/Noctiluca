package noctiluca.features.authentication.viewmodel.instancedetail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.UnknownException
import noctiluca.features.authentication.viewmodel.context.SignInFeatureContext
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.instance.model.Instance
import noctiluca.model.Domain
import noctiluca.model.Uri
import org.koin.core.component.get

interface AuthorizeViewModel {
    companion object {
        operator fun invoke(
            clientName: String,
            redirectUri: Uri,
            navigation: SignInNavigation?,
            coroutineScope: CoroutineScope,
            lifecycleRegistry: LifecycleRegistry,
            context: SignInFeatureContext.Child.MastodonInstanceDetail,
        ): AuthorizeViewModel = Impl(
            clientName,
            redirectUri,
            navigation,
            context.get(),
            context.get(),
            coroutineScope,
            lifecycleRegistry,
            context,
        )
    }

    fun requestAuthorize(instance: Instance)
    fun fetchAccessToken(result: AuthorizeResult?)

    private class Impl(
        private val clientName: String,
        private val redirectUri: Uri,
        private val navigation: SignInNavigation?,
        private val requestAppCredentialUseCase: RequestAppCredentialUseCase,
        private val requestRequestAccessTokenUseCase: RequestAccessTokenUseCase,
        coroutineScope: CoroutineScope,
        lifecycleRegistry: LifecycleRegistry,
        context: SignInFeatureContext.Child.MastodonInstanceDetail,
    ) : AuthorizeViewModel, ViewModel(coroutineScope, lifecycleRegistry, context) {
        private val authorizationLoadState by lazy { MutableValue<LoadState>(LoadState.Initial) }

        override fun requestAuthorize(instance: Instance) {
            val domain = Domain(instance.domain)

            val job = launchLazy {
                runCatching { requestAppCredentialUseCase.execute(domain, clientName, redirectUri) }
                    .onSuccess { /*navigation?.openBrowser(it)*/ }
                    .onFailure { authorizationLoadState.value = LoadState.Error(it) }
            }

            authorizationLoadState.value = LoadState.Loading(job)
            job.start()
        }

        override fun fetchAccessToken(result: AuthorizeResult?) {
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
    }
}
