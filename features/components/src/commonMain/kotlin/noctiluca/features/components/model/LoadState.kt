package noctiluca.features.components.model

import kotlinx.coroutines.Job

sealed class LoadState {
    object Initial : LoadState()
    data class Loaded<T: Any>(val value: T) : LoadState()
    data class Error(val cause: Throwable) : LoadState()
    class Loading(val job: Job) : LoadState()

    val loading get() = this is Loading
    val loaded get() = this !is Initial && this !is Loading

    @Suppress("UNCHECKED_CAST")
    fun <T> getValueOrNull(): T? = if (this is Loaded<*>) value as T else null
    fun getErrorOrNull() = if (this is Error) cause else null
    fun cancel() { if (this is Loading) job.cancel() }
}
