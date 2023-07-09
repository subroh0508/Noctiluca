package noctiluca.features.accountdetail.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.features.accountdetail.AccountDetailNavigator
import noctiluca.features.components.AuthorizedViewModel
import noctiluca.features.components.LocalCoroutineExceptionHandler
import noctiluca.features.components.UnauthorizedExceptionHandler
import noctiluca.features.components.model.LoadState
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.status.model.Status
import org.koin.core.component.get

class AccountDetailViewModel private constructor(
    val id: AccountId,
    private val fetchAccountAttributesUseCase: FetchAccountAttributesUseCase,
    private val fetchAccountStatusesUseCase: FetchAccountStatusesUseCase,
    coroutineScope: CoroutineScope,
    exceptionHandler: UnauthorizedExceptionHandler,
) : AuthorizedViewModel(coroutineScope, exceptionHandler) {
    private val mutableUiModel by lazy { MutableValue(UiModel()) }

    private val tab by lazy {
        MutableValue(UiModel.Tab.STATUSES).also {
            it.subscribe { tab ->
                mutableUiModel.value = uiModel.value.copy(tab = tab)
            }
        }
    }
    private val statuses by lazy {
        MutableValue(listOf<Status>()).also {
            it.subscribe { statuses ->
                val current = uiModel.value.statuses[tab.value] ?: listOf()

                mutableUiModel.value = uiModel.value.copy(
                    statuses = uiModel.value.statuses + mapOf(tab.value to current + statuses),
                )
            }
        }
    }

    val uiModel: Value<UiModel> = mutableUiModel

    fun load() {
        val job = launchLazy {
            runCatchingWithAuth { fetchAccountAttributesUseCase.execute(id) }
                .onSuccess { mutableUiModel.value = uiModel.value.copy(account = LoadState.Loaded(it)) }
                .onFailure { mutableUiModel.value = uiModel.value.copy(account = LoadState.Error(it)) }
        }

        mutableUiModel.value = uiModel.value.copy(account = LoadState.Loading(job))
        job.start()
    }

    fun switch(tab: UiModel.Tab) {
        this.tab.value = tab
    }

    fun refreshStatuses() {
        val tabs = UiModel.Tab.values()

        tabs.forEach { t ->
            if (uiModel.value.statuses[t]?.isNotEmpty() == true) {
                return@forEach
            }

            launch {
                runCatchingWithAuth {
                    fetchAccountStatusesUseCase.execute(
                        id,
                        t.buildQuery(),
                    )
                }
                    .onSuccess {
                        val current = uiModel.value

                        mutableUiModel.value = current.copy(
                            statuses = current.statuses + mapOf(t to it),
                        )
                    }
                    .onFailure { }
            }
        }
    }

    fun loadStatusesMore() {
        val tab = uiModel.value.tab
        val foregroundStatuses = uiModel.value.foreground

        launch {
            runCatchingWithAuth {
                fetchAccountStatusesUseCase.execute(
                    id,
                    tab.buildQuery(foregroundStatuses.lastOrNull()?.id),
                )
            }
                .onSuccess { statuses.value = it }
                .onFailure { }
        }
    }

    data class UiModel(
        val account: LoadState = LoadState.Initial,
        val tab: Tab = Tab.STATUSES,
        val statuses: Map<Tab, List<Status>> = mapOf(),
    ) {
        enum class Tab {
            STATUSES, STATUSES_AND_REPLIES, MEDIA;

            fun buildQuery(maxId: StatusId? = null) = when (this) {
                STATUSES -> StatusesQuery.Default(maxId = maxId)
                STATUSES_AND_REPLIES -> StatusesQuery.WithReplies(maxId = maxId)
                MEDIA -> StatusesQuery.OnlyMedia(maxId = maxId)
            }
        }

        val foreground get() = statuses[tab] ?: listOf()
    }

    companion object Provider {
        @Composable
        operator fun invoke(
            context: AccountDetailNavigator.Screen,
        ): AccountDetailViewModel {
            val coroutineScope = rememberCoroutineScope()
            val handler = LocalCoroutineExceptionHandler.current

            return remember {
                AccountDetailViewModel(
                    context.id,
                    context.get(),
                    context.get(),
                    coroutineScope,
                    handler,
                )
            }
        }
    }
}
