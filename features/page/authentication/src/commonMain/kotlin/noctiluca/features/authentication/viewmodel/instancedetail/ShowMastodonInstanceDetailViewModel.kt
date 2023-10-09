package noctiluca.features.authentication.viewmodel.instancedetail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.features.authentication.SignInNavigator
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import org.koin.core.component.get

interface ShowMastodonInstanceDetailViewModel {
    companion object {
        operator fun invoke(
            domain: String,
            coroutineScope: CoroutineScope,
            context: SignInNavigator.Screen,
        ): ShowMastodonInstanceDetailViewModel = Impl(
            domain,
            context.get(),
            context.get(),
            coroutineScope,
        )
    }

    val uiModel: Value<UiModel>

    fun load()
    fun loadMore()

    data class UiModel(
        val instance: LoadState = LoadState.Initial,
        val statuses: List<Status> = listOf(),
    )

    private class Impl(
        private val domain: String,
        private val fetchMastodonInstanceUseCase: FetchMastodonInstanceUseCase,
        private val fetchLocalTimelineUseCase: FetchLocalTimelineUseCase,
        coroutineScope: CoroutineScope,
    ) : ShowMastodonInstanceDetailViewModel, ViewModel(coroutineScope) {
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

        override val uiModel: Value<UiModel> = mutableUiModel

        override fun load() {
            loadInstanceDetail()
            loadLocalTimeline()
        }

        override fun loadMore() = loadLocalTimeline(uiModel.value.statuses.lastOrNull()?.id)

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
                            if (maxId == null) {
                                it
                            } else {
                                statuses.value + it
                            }
                    }
                    .onFailure { statuses.value = listOf() }
            }
        }
    }
}
