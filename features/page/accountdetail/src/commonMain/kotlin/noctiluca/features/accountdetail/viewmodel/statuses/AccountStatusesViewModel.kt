package noctiluca.features.accountdetail.viewmodel.statuses

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.features.accountdetail.AccountDetailNavigator
import noctiluca.features.components.AuthorizedViewModel
import noctiluca.features.components.UnauthorizedExceptionHandler
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.status.model.Status
import org.koin.core.component.get

interface AccountStatusesViewModel {
    companion object {
        operator fun invoke(
            id: AccountId,
            coroutineScope: CoroutineScope,
            lifecycleRegistry: LifecycleRegistry,
            context: AccountDetailNavigator.Child.AccountDetail,
            exceptionHandler: UnauthorizedExceptionHandler,
        ): AccountStatusesViewModel = Impl(
            id,
            context.get(),
            coroutineScope,
            lifecycleRegistry,
            context,
            exceptionHandler,
        )
    }

    val uiModel: Value<UiModel>

    fun switch(tab: UiModel.Tab)
    fun refresh()
    fun loadMore()

    data class UiModel(
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

    private class Impl(
        private val id: AccountId,
        private val fetchAccountStatusesUseCase: FetchAccountStatusesUseCase,
        coroutineScope: CoroutineScope,
        lifecycleRegistry: LifecycleRegistry,
        context: AccountDetailNavigator.Child.AccountDetail,
        exceptionHandler: UnauthorizedExceptionHandler,
    ) : AccountStatusesViewModel, AuthorizedViewModel(
        coroutineScope,
        lifecycleRegistry,
        context,
        exceptionHandler,
    ) {
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

        override val uiModel: Value<UiModel> = mutableUiModel

        override fun switch(tab: UiModel.Tab) {
            this.tab.value = tab
        }

        override fun refresh() {
            val tabs = UiModel.Tab.values()

            tabs.forEach { t ->
                if (uiModel.value.statuses[t]?.isNotEmpty() == true) {
                    return@forEach
                }

                launchLazy {
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

        override fun loadMore() {
            val tab = uiModel.value.tab
            val foregroundStatuses = uiModel.value.foreground

            launchLazy {
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
    }
}
