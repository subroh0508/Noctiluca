package noctiluca.features.accountdetail.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.*
import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AccountDetailViewModel private constructor(
    val id: AccountId,
    private val fetchAccountAttributesUseCase: FetchAccountAttributesUseCase,
    private val fetchAccountStatusesUseCase: FetchAccountStatusesUseCase,
    authorizedUserRepository: AuthorizedUserRepository,
) : AuthorizedViewModel(authorizedUserRepository) {
    private val accountDetailLoadState by lazy { MutableStateFlow<LoadState>(LoadState.Initial) }
    private val tab by lazy { MutableStateFlow(UiModel.Tab.STATUSES) }
    private val statuses by lazy { MutableStateFlow<Map<UiModel.Tab, List<Status>>>(mapOf()) }

    val uiModel by lazy {
        combine(
            accountDetailLoadState,
            tab,
            statuses,
        ) { accountDetailLoadState, tab, statuses ->
            UiModel(
                accountDetailLoadState,
                tab = tab,
                statuses = statuses,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiModel(),
        )
    }

    fun load() {
        val job = launchLazy {
            runCatchingWithAuth { fetchAccountAttributesUseCase.execute(id) }
                .onSuccess { accountDetailLoadState.value = LoadState.Loaded(it) }
                .onFailure { accountDetailLoadState.value = LoadState.Error(it) }
        }

        accountDetailLoadState.value = LoadState.Loading(job)
        job.start()
    }

    fun switch(tab: UiModel.Tab) {
        this.tab.value = tab
    }

    fun refreshStatuses() {
        val tabs = UiModel.Tab.entries.toTypedArray()

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
                    .onSuccess { statuses.value += mapOf(t to it) }
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
                .onSuccess {
                    val current = statuses.value[tab] ?: listOf()

                    statuses.value += mapOf(tab to current + it)
                }
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
            id: AccountId,
            component: KoinComponent,
        ): AccountDetailViewModel {
            return remember {
                AccountDetailViewModel(
                    id,
                    component.get(),
                    component.get(),
                    component.get(),
                )
            }
        }
    }
}
