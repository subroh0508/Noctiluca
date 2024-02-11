package noctiluca.features.shared.model

import kotlinx.coroutines.Job

sealed class LoadState {
    data object Initial : LoadState()
    data object Loaded : LoadState()
    data class Error(val cause: Throwable) : LoadState()
    class Loading(val job: Job) : LoadState()

    val loading get() = this is Loading
    val loaded get() = this !is Initial && this !is Loading

    fun getErrorOrNull() = if (this is Error) cause else null
    fun cancel() {
        if (this is Loading) job.cancel()
    }
}
