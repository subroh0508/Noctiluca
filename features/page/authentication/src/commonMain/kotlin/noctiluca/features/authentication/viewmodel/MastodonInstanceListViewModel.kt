package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MastodonInstanceListViewModel(
    private val searchMastodonInstancesUseCase: SearchMastodonInstancesUseCase,
) : ViewModel() {
    private val instanceSuggests by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }
    private val query by lazy { MutableStateFlow("") }

    val uiModel by lazy {
        combine(
            instanceSuggests,
            query,
        ) { suggests, query -> UiModel(query, suggests) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = UiModel(),
            )
    }

    fun search(query: String) {
        val prevQuery = uiModel.value.query

        if (query == prevQuery) {
            return
        }

        this.query.value = query
        if (query.isBlank()) {
            instanceSuggests.value = LoadState.Initial
            return
        }

        val job = launchLazy {
            runCatching { searchMastodonInstancesUseCase.execute(query) }
                .onSuccess { instanceSuggests.value = LoadState.Loaded(it) }
                .onFailure { instanceSuggests.value = LoadState.Error(it) }
        }

        instanceSuggests.value = LoadState.Loading(job)
        job.start()
    }

    data class UiModel(
        val query: String = "",
        val suggests: LoadState = LoadState.Initial,
    ) : ScreenModel

    companion object Provider {
        @Composable
        operator fun invoke(
            koinComponent: KoinComponent,
        ): MastodonInstanceListViewModel {
            return remember {
                MastodonInstanceListViewModel(
                    koinComponent.get(),
                )
            }
        }
    }
}
