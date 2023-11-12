package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.UnknownException
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.components.ViewModel
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authentication.Instance
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AuthorizeViewModel private constructor(
    private val clientName: String,
    private val redirectUri: Uri,
    private val requestAppCredentialUseCase: RequestAppCredentialUseCase,
    private val requestRequestAccessTokenUseCase: RequestAccessTokenUseCase,
    coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {
    private val authorizationLoadState by lazy { MutableValue<Event>(Event.Initial) }

    val event: Value<Event> = authorizationLoadState
    val isFetchingAccessToken get() = authorizationLoadState.value.isFetchingAccessToken

    fun requestAuthorize(instance: Instance) {
        val domain = Domain(instance.domain)

        val job = launchLazy {
            runCatching { requestAppCredentialUseCase.execute(domain, clientName, redirectUri) }
                .onSuccess { authorizationLoadState.value = Event.OpeningBrowser(it) }
                .onFailure { authorizationLoadState.value = Event.Error(it) }
        }

        authorizationLoadState.value = Event.Loading(job)
        job.start()
    }

    fun fetchAccessToken(result: AuthorizeResult?) {
        val code = result?.getCodeOrNull()
        val error = result?.getErrorOrNull()

        if (authorizationLoadState.value !is Event.OpeningBrowser) {
            return
        }

        if (code == null) {
            authorizationLoadState.value = error?.let(Event::Error) ?: Event.Initial
            return
        }

        val job = launchLazy {
            runCatching { requestRequestAccessTokenUseCase.execute(code, redirectUri) }
                .onSuccess {
                    if (it != null) {
                        authorizationLoadState.value = Event.NavigatingToTimelines
                        // navigator?.navigateToTimelines()
                        return@onSuccess
                    }

                    authorizationLoadState.value = Event.Error(UnknownException)
                }
                .onFailure { authorizationLoadState.value = Event.Error(it) }
        }

        authorizationLoadState.value = Event.Loading(job)
        job.start()
    }

    sealed class Event {
        data object Initial : Event()
        data class Loading(val job: Job) : Event()
        data class Error(val cause: Throwable) : Event()

        data class OpeningBrowser(val uri: Uri) : Event()
        data object NavigatingToTimelines : Event()

        val isFetchingAccessToken get() = this !is Initial && this !is Error
    }

    companion object Provider {
        @Composable
        operator fun invoke(
            domain: String,
            koinComponent: KoinComponent,
        ): AuthorizeViewModel {
            val clientName = getString().sign_in_client_name
            val redirectUri = buildRedirectUri(domain)
            val coroutineScope = rememberCoroutineScope()

            return remember {
                AuthorizeViewModel(
                    clientName,
                    redirectUri,
                    koinComponent.get(),
                    koinComponent.get(),
                    coroutineScope,
                )
            }
        }
    }
}
