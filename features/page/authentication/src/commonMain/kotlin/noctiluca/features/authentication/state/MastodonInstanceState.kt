package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.model.QueryText
import org.koin.core.scope.Scope

@Composable
internal fun rememberMastodonInstance(
    query: QueryText,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val useCase: ShowMastodonInstanceUseCase = remember { scope.get() }

    return produceState<LoadState>(
        initialValue = LoadState.Initial,
        query.text,
    ) {
        if (query !is QueryText.Static) {
            value = LoadState.Initial
            return@produceState
        }

        val job = launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.execute(query.text) }
                .onSuccess { value = LoadState.Loaded(it) }
                .onFailure { value = LoadState.Error(it) }
        }

        value = LoadState.Loading(job)
        job.start()
    }
}
