package noctiluca.features.authentication.viewmodel.instancedetail

import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.features.authentication.SignInNavigator
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.UnknownException
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
            navigator: SignInNavigator?,
            coroutineScope: CoroutineScope,
            context: SignInNavigator.Screen,
        ): AuthorizeViewModel = Impl(
            clientName,
            redirectUri,
            navigator,
            context.get(),
            context.get(),
            coroutineScope,
        )
    }

    val loading: Boolean

    fun requestAuthorize(instance: Instance)
    fun fetchAccessToken(result: AuthorizeResult?)

    private class Impl(
        private val clientName: String,
        private val redirectUri: Uri,
        private val navigator: SignInNavigator?,
        private val requestAppCredentialUseCase: RequestAppCredentialUseCase,
        private val requestRequestAccessTokenUseCase: RequestAccessTokenUseCase,
        coroutineScope: CoroutineScope,
    ) : AuthorizeViewModel, ViewModel(coroutineScope) {
        private val authorizationLoadState by lazy { MutableValue<LoadState>(LoadState.Initial) }

        override val loading get() = authorizationLoadState.value.loading

        override fun requestAuthorize(instance: Instance) {
            val domain = Domain(instance.domain)

            val job = launchLazy {
                runCatching { requestAppCredentialUseCase.execute(domain, clientName, redirectUri) }
                    .onSuccess { navigator?.openBrowser(it) }
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
                            navigator?.navigateToTimelines()
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
