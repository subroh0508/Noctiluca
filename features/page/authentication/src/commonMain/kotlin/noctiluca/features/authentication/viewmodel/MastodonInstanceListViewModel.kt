package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MastodonInstanceListViewModel(
    coroutineScope: CoroutineScope,
    private val searchMastodonInstancesUseCase: SearchMastodonInstancesUseCase,
) : ViewModel(coroutineScope), ScreenModel {
    private val mutableUiModel by lazy { MutableValue(UiModel()) }
    private val mutableInstanceSuggests by lazy {
        MutableValue<LoadState>(LoadState.Initial).also {
            it.subscribe { loadState ->
                mutableUiModel.value = uiModel.value.copy(suggests = loadState)
            }
        }
    }

    val uiModel: Value<UiModel> = mutableUiModel

    fun search(query: String) {
        val prevQuery = uiModel.value.query

        if (query == prevQuery) {
            return
        }

        mutableUiModel.value = uiModel.value.copy(query = query)
        if (query.isBlank()) {
            mutableInstanceSuggests.value = LoadState.Initial
            return
        }

        val job = screenModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { searchMastodonInstancesUseCase.execute(query) }
                .onSuccess { mutableInstanceSuggests.value = LoadState.Loaded(it) }
                .onFailure { mutableInstanceSuggests.value = LoadState.Error(it) }
        }

        mutableInstanceSuggests.value = LoadState.Loading(job)
        job.start()
    }

    data class UiModel(
        val query: String = "",
        val suggests: LoadState = LoadState.Initial,
    ) : ScreenModel

    companion object Provider {
        private const val UI_MODEL_KEEPER = "MastodonInstanceListViewModel.UiModel"

        @Composable
        operator fun invoke(
            koinComponent: KoinComponent,
        ): MastodonInstanceListViewModel {
            val coroutineScope = rememberCoroutineScope()

            return remember {
                MastodonInstanceListViewModel(
                    coroutineScope,
                    koinComponent.get(),
                )
            }
        }
    }
}
