package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.di.SignInFeatureContext
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.instance.model.Instance

class MastodonInstanceListViewModel private constructor(
    private val searchMastodonInstancesUseCase: SearchMastodonInstancesUseCase,
    suggests: List<Instance.Suggest>,
    coroutineScope: CoroutineScope,
    lifecycleRegistry: LifecycleRegistry,
    componentContext: ComponentContext,
) : ViewModel(coroutineScope, lifecycleRegistry, componentContext) {
    private val mutableUiModel by lazy { MutableValue(UiModel()) }

    private val mutableInstanceSuggests by lazy {
        MutableValue<LoadState>(LoadState.Loaded(suggests)).also {
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

        if (query.isBlank()) {
            mutableUiModel.value = uiModel.value.copy(query = query)
            mutableInstanceSuggests.value = LoadState.Initial
            return
        }

        val job = launchLazy {
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
    )

    companion object Factory {
        @Composable
        operator fun invoke(
            suggests: List<Instance.Suggest>,
            lifecycleRegistry: LifecycleRegistry,
            context: ComponentContext,
        ): MastodonInstanceListViewModel {
            val koinScope = LocalScope.current
            val coroutineScope = rememberCoroutineScope()

            return remember {
                MastodonInstanceListViewModel(
                    koinScope.get(),
                    suggests,
                    coroutineScope,
                    lifecycleRegistry,
                    context,
                )
            }
        }
    }
}
