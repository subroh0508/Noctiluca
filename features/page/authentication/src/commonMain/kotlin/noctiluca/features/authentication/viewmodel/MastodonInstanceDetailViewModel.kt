package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.model.StatusId
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
                mutableUiModel.value = uiModel.value.copy(instance = loadState)
            }
        }
    }
    private val statuses by lazy {
        MutableValue(listOf<Status>()).also {
            it.subscribe { statuses ->
                mutableUiModel.value = uiModel.value.copy(statuses = statuses)
            }
        }
    }

    val uiModel: Value<UiModel> = mutableUiModel

    fun load() {
        loadInstanceDetail()
        loadLocalTimeline()
    }

    fun loadMore() = loadLocalTimeline(uiModel.value.statuses.lastOrNull()?.id)

    private fun loadInstanceDetail() {
        val job = launchLazy {
            runCatching { fetchMastodonInstanceUseCase.execute(domain) }
                .onSuccess { instanceLoadState.value = LoadState.Loaded(it) }
                .onFailure { instanceLoadState.value = LoadState.Error(it) }
        }

        instanceLoadState.value = LoadState.Loading(job)
        job.start()
    }

    private fun loadLocalTimeline(maxId: StatusId? = null) {
        if (maxId == null && uiModel.value.statuses.isNotEmpty()) {
            return
        }

        launch {
            runCatching { fetchLocalTimelineUseCase.execute(domain, maxId) }
                .onSuccess {
                    statuses.value =
                        if (maxId == null)
                            it
                        else
                            statuses.value + it
                }
                .onFailure { statuses.value = listOf() }
        }
    }

    data class UiModel(
        val instance: LoadState = LoadState.Initial,
        val statuses: List<Status> = listOf(),
    )

    companion object Factory {
        @Composable
        operator fun invoke(
            domain: String,
            lifecycleRegistry: LifecycleRegistry,
            context: ComponentContext,
        ): MastodonInstanceDetailViewModel {
            val koinScope = LocalScope.current
            val coroutineScope = rememberCoroutineScope()
            /*
            val childContext = remember {
                context.childContext(
                    "MastodonInstanceDetail",
                    lifecycleRegistry,
                )
            }
            */

            return remember(domain) {
                MastodonInstanceDetailViewModel(
                    domain,
                    koinScope.get(),
                    koinScope.get(),
                    coroutineScope,
                    lifecycleRegistry,
                    context,
                )
            }
        }
    }
}
