package noctiluca.features.signin.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import noctiluca.data.authorization.AuthorizationRepository
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.signin.model.AuthorizeResult
import noctiluca.features.signin.model.UnknownException
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authorization.Instance

class AuthorizeViewModel(
    private val clientName: String,
    private val redirectUri: Uri,
    private val repository: AuthorizationRepository,
) : ViewModel(), ScreenModel {
    private val mutableEvent by lazy { MutableStateFlow<Event>(Event.Initial) }

    val event: StateFlow<Event> = mutableEvent
    val isFetchingAccessToken get() = mutableEvent.value.isFetchingAccessToken

    fun requestAuthorize(instance: Instance) {
        val domain = Domain(instance.domain)

        val job = launchLazy {
            runCatching { repository.fetchAuthorizeUrl(domain, clientName, redirectUri) }
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
            runCatching { fetchAccessToken(code, redirectUri) }
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

    private suspend fun fetchAccessToken(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser? {
        val user = repository.fetchAccessToken(code, redirectUri) ?: return null
        return repository.switchAccessToken(user.id)
    }

    sealed class Event {
        data object Initial : Event()
        data class Loading(val job: Job) : Event()
        data class Error(val cause: Throwable) : Event()

        data class OpeningBrowser(val uri: Uri) : Event()
        data object NavigatingToTimelines : Event()

        val isFetchingAccessToken get() = this !is Initial && this !is Error
    }
}
