package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.components.model.LoadState
import noctiluca.components.state.loadLazy
import noctiluca.components.state.produceLoadState
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.model.QueryText
import org.koin.core.scope.Scope

@Composable
internal fun rememberMastodonInstance(
    query: QueryText,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val useCase: ShowMastodonInstanceUseCase = remember { scope.get() }

    return produceLoadState(query.text) {
        if (query !is QueryText.Static) {
            value = LoadState.Initial
            return@produceLoadState
        }

        loadLazy { useCase.execute(query.text)  }
    }
}
