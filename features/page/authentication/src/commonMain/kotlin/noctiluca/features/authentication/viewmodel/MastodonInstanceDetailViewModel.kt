package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.instance.model.Instance
import noctiluca.status.model.Status

class MastodonInstanceDetailViewModel private constructor(
    private val domain: String,
    private val fetchMastodonInstanceUseCase: FetchMastodonInstanceUseCase,
    private val fetchLocalTimelineUseCase: FetchLocalTimelineUseCase,
    coroutineScope: CoroutineScope,
    lifecycleRegistry: LifecycleRegistry,
    componentContext: ComponentContext,
) : ViewModel(coroutineScope, lifecycleRegistry, componentContext) {
    private val mutableUiModel by lazy { MutableValue(UiModel()) }
    private val instanceLoadState by lazy {
        MutableValue<LoadState>(LoadState.Initial).also {
            it.subscribe { loadState ->
                mutableUiModel.value = uiModel.value.copy(
                    instance = loadState.getValueOrNull(),
                    loading = loadState.loading,
                    error = loadState.getErrorOrNull(),
                )
            }
        }
    }
    private val statusesLoadState by lazy {
        MutableValue(listOf<Status>()).also {
            it.subscribe { statuses ->
                mutableUiModel.value = uiModel.value.copy(
                    statuses = statuses,
                )
            }
        }
    }

    val uiModel: Value<UiModel> = mutableUiModel

    fun load() {
        loadInstanceDetail()
        loadLocalTimeline()
    }

    private fun loadInstanceDetail() {
        val job = launchLazy {
            runCatching { fetchMastodonInstanceUseCase.execute(domain) }
                .onSuccess { instanceLoadState.value = LoadState.Loaded(it) }
                .onFailure { instanceLoadState.value = LoadState.Error(it) }
        }

        instanceLoadState.value = LoadState.Loading(job)
        job.start()
    }

    private fun loadLocalTimeline() {
        launch {
            runCatching { fetchLocalTimelineUseCase.execute(domain) }
                .onSuccess { statusesLoadState.value = it }
                .onFailure { statusesLoadState.value = listOf() }
        }
    }

    data class UiModel(
        val instance: Instance? = null,
        val statuses: List<Status> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null,
    )

    companion object Factory {
        @Composable
        fun invoke(
            domain: String,
            lifecycleRegistry: LifecycleRegistry,
            context: ComponentContext,
        ): MastodonInstanceDetailViewModel {
            val koinScope = LocalScope.current

            return MastodonInstanceDetailViewModel(
                domain,
                remember { koinScope.get() },
                remember { koinScope.get() },
                rememberCoroutineScope(),
                lifecycleRegistry,
                context.childContext(
                    "MastodonInstanceDetail",
                    lifecycleRegistry,
                )
            )
        }
    }
}
