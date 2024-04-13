package noctiluca.features.shared.model

import kotlinx.coroutines.Job

data class MessageHolder<E : Enum<*>>(
    val text: E? = null,
    val error: Throwable? = null,
    val job: Job? = null,
) {
    fun consume() = copy(
        text = null,
        error = null,
        job = null,
    )

    val isLoading get() = job != null
}
