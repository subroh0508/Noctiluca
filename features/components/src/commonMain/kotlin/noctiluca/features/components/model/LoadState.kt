package noctiluca.features.components.model

import androidx.compose.runtime.Composable
import kotlinx.coroutines.Job

sealed class LoadState {
    data object Initial : LoadState()
    data class Loaded<T : Any>(val value: T) : LoadState()
    data class Error(val cause: Throwable) : LoadState()
    class Loading(val job: Job) : LoadState()

    val loading get() = this is Loading
    val loaded get() = this !is Initial && this !is Loading

    @Suppress("UNCHECKED_CAST")
    fun <T> getValueOrNull(): T? = if (this is Loaded<*>) value as T else null
    fun getErrorOrNull() = if (this is Error) cause else null
    fun cancel() {
        if (this is Loading) job.cancel()
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Any> LoadStateComposable(
    loadState: LoadState,
    loading: @Composable (Job) -> Unit,
    fallback: @Composable (Throwable?) -> Unit,
    content: @Composable (T) -> Unit,
) = when (loadState) {
    LoadState.Initial -> fallback(null)
    is LoadState.Loading -> loading(loadState.job)
    is LoadState.Loaded<*> -> content(loadState.value as T)
    is LoadState.Error -> fallback(loadState.cause)
}
