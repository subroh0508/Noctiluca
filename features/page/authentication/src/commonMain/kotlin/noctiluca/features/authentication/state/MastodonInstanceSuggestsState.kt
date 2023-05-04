package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.components.model.LoadState
import noctiluca.features.components.state.loadLazy
import noctiluca.features.components.state.produceLoadState
import org.koin.core.scope.Scope

@Composable
internal fun rememberMastodonInstanceSuggests(
    query: String,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val useCase: SearchMastodonInstancesUseCase = remember { scope.get() }

    return produceLoadState(query) {
        if (query.isBlank()) {
            value = LoadState.Initial
            return@produceLoadState
        }

        loadLazy { useCase.execute(query) }
    }
}
