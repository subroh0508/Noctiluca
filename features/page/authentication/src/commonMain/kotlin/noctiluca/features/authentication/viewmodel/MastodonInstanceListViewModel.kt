package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.features.authentication.SignInNavigator
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import org.koin.core.component.get

class MastodonInstanceListViewModel private constructor(
    private val searchMastodonInstancesUseCase: SearchMastodonInstancesUseCase,
    coroutineScope: CoroutineScope,
    screen: SignInNavigator.Screen,
) : ViewModel(coroutineScope), ComponentContext by screen {
    private val mutableUiModel by lazy {
        MutableValue(cachedUiModel ?: UiModel()).also {
            it.subscribe { model ->
                if (cachedUiModel != null) {
                    instanceKeeper.remove(UI_MODEL_KEEPER)
                }

                instanceKeeper.put(UI_MODEL_KEEPER, model)
            }
        }
    }

    private val mutableInstanceSuggests by lazy {
        MutableValue(cachedUiModel?.suggests ?: LoadState.Initial).also {
            it.subscribe { loadState ->
                mutableUiModel.value = uiModel.value.copy(suggests = loadState)
            }
        }
    }

    val uiModel: Value<UiModel> = mutableUiModel

    private val cachedUiModel get() = instanceKeeper.get(UI_MODEL_KEEPER) as? UiModel

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
    ) : InstanceKeeper.Instance {
        override fun onDestroy() = Unit
    }

    companion object Provider {
        private const val UI_MODEL_KEEPER = "MastodonInstanceListViewModel.UiModel"

        @Composable
        operator fun invoke(
            context: SignInNavigator.Screen,
        ): MastodonInstanceListViewModel {
            val coroutineScope = rememberCoroutineScope()

            return remember {
                MastodonInstanceListViewModel(
                    context.get(),
                    coroutineScope,
                    context,
                )
            }
        }
    }
}
