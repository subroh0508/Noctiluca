package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.status.model.Status
import org.koin.core.scope.Scope

class LocalTimelineState(
    private val fetchLocalTimelineUseCase: FetchLocalTimelineUseCase,
    private val state: MutableState<List<Status>> = mutableStateOf(listOf())
) : State<List<Status>> by state {
    constructor(koinScope: Scope) : this(
        fetchLocalTimelineUseCase = koinScope.get(),
    )

    fun load(scope: CoroutineScope, domain: String) {
        if (state.value.isNotEmpty()) {
            return
        }

        scope.launch {
            runCatching { fetchLocalTimelineUseCase.execute(domain) }
                .onSuccess { state.value = it }
                .onFailure { }
        }
    }

    fun loadMore(scope: CoroutineScope, domain: String) {
        val maxId = state.value.lastOrNull()?.id ?: return

        scope.launch {
            runCatching { fetchLocalTimelineUseCase.execute(domain, maxId) }
                .onSuccess { state.value += it }
                .onFailure { }
        }
    }
}

@Composable
internal fun rememberLocalTimelineState(
    domain: String,
    scope: Scope = LocalScope.current,
): LocalTimelineState {
    val statuses = remember { LocalTimelineState(scope) }

    LaunchedEffect(Unit) {
        statuses.load(this, domain)
    }

    return statuses
}
