package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.UnknownException
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.shared.viewmodel.ViewModel
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
    private val mutableEvent by lazy { MutableStateFlow<Event>(Event.Initial) }

    val event: StateFlow<Event> = mutableEvent
    val isFetchingAccessToken get() = mutableEvent.value.isFetchingAccessToken

    fun requestAuthorize(instance: Instance) {
        val domain = Domain(instance.domain)

        val job = launchLazy {
            runCatching { requestAppCredentialUseCase.execute(domain, clientName, redirectUri) }
                .onSuccess { mutableEvent.value = Event.OpeningBrowser(it) }
                .onFailure { mutableEvent.value = Event.Error(it) }
        }

        mutableEvent.value = Event.Loading(job)
        job.start()
    }

    fun fetchAccessToken(result: AuthorizeResult?) {
        val code = result?.getCodeOrNull()
        val error = result?.getErrorOrNull()

        if (mutableEvent.value !is Event.OpeningBrowser) {
            return
        }

        if (code == null) {
            mutableEvent.value = error?.let(Event::Error) ?: Event.Initial
            return
        }

        val job = launchLazy {
            runCatching { requestRequestAccessTokenUseCase.execute(code, redirectUri) }
                .onSuccess {
                    if (it != null) {
                        mutableEvent.value = Event.NavigatingToTimelines
                        return@onSuccess
                    }

                    mutableEvent.value = Event.Error(UnknownException)
                }
                .onFailure { mutableEvent.value = Event.Error(it) }
        }

        mutableEvent.value = Event.Loading(job)
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
