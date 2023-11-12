package noctiluca.features.authentication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.features.authentication.LocalNavigator
import noctiluca.features.authentication.SignInNavigator
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.authentication.viewmodel.instancedetail.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.instancedetail.ShowMastodonInstanceDetailViewModel
import noctiluca.features.components.ViewModel
import noctiluca.features.components.model.LoadState
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.model.status.Status
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MastodonInstanceDetailViewModel private constructor(
    val domain: String,
    private val fetchMastodonInstanceUseCase: FetchMastodonInstanceUseCase,
    private val fetchLocalTimelineUseCase: FetchLocalTimelineUseCase,
    coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {
    private val mutableUiModel by lazy { MutableValue(ShowMastodonInstanceDetailViewModel.UiModel()) }
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

    val uiModel: Value<ShowMastodonInstanceDetailViewModel.UiModel> = mutableUiModel

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
                        if (maxId == null) {
                            it
                        } else {
                            statuses.value + it
                        }
                }
                .onFailure { statuses.value = listOf() }
        }
    }

    companion object Provider {
        @Composable
        operator fun invoke(
            domain: String,
            koinComponent: KoinComponent,
        ): MastodonInstanceDetailViewModel {
            val coroutineScope = rememberCoroutineScope()

            return remember {
                MastodonInstanceDetailViewModel(
                    domain,
                    koinComponent.get(),
                    koinComponent.get(),
                    coroutineScope,
                )
            }
        }
    }
}
