package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.instance.model.Instance
import org.koin.core.scope.Scope

data class Instances(
    val instance: Instance? = null,
    val suggests: List<Instance.Suggest> = listOf(),
    val job: Job? = null,
    val error: Throwable? = null,
    val loaded: Boolean = false,
) : List<Instance.Suggest> by suggests {
    enum class Tab {
        INFO, EXTENDED_DESCRIPTION, LOCAL_TIMELINE
    }

    val loading get() = job != null
}

class MastodonInstanceState(
    private val searchMastodonInstancesUseCase: SearchMastodonInstancesUseCase,
    private val showMastodonInstanceUseCase: ShowMastodonInstanceUseCase,
    private val state: MutableState<Instances> = mutableStateOf(Instances()),
) : MutableState<Instances> by state {
    constructor(koinScope: Scope) : this(
        koinScope.get(),
        koinScope.get(),
    )

    var instance: Instance?
        get() = value.instance
        private set(value) {
            state.value = state.value.copy(
                job = null,
                instance = value,
                loaded = value != null,
            )
        }

    var suggests: List<Instance.Suggest>
        get() = value
        private set(value) {
            state.value = state.value.copy(
                job = null,
                suggests = value,
                loaded = true,
            )
        }

    val loading get() = state.value.loading
    val loaded get() = state.value.loaded

    fun search(scope: CoroutineScope, query: String) {
        if (query.isBlank()) {
            clearSuggests()
            return
        }

        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatching { searchMastodonInstancesUseCase.execute(query) }
                .onSuccess { suggests = it }
                .onFailure { reset(error = it) }
        }

        reset(job = job)
        job.start()
    }

    fun clearSuggests() {
        state.value = Instances()
    }

    fun select(scope: CoroutineScope, suggest: Instance.Suggest) {
        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatching { showMastodonInstanceUseCase.execute(suggest.domain) }
                .onSuccess { instance = it }
                .onFailure { reset(error = it) }
        }

        reset(job = job)
        job.start()
    }

    fun clearInstance() {
        instance = null
    }

    private fun reset(
        job: Job? = null,
        error: Throwable? = null,
    ) {
        state.value = state.value.copy(
            job = job,
            error = error,
            loaded = job == null,
        )
    }
}

@Composable
internal fun rememberMastodonInstancesState(
    scope: Scope = LocalScope.current,
) = remember { MastodonInstanceState(scope) }
