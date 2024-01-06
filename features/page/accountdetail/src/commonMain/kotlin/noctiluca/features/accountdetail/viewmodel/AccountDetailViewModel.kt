package noctiluca.features.accountdetail.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.status.Status
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AccountDetailViewModel(
    val id: AccountId,
    private val accountDetailRepository: AccountDetailRepository,
    private val fetchAccountStatusesUseCase: FetchAccountStatusesUseCase,
    authorizedUserRepository: AuthorizedUserRepository,
) : AuthorizedViewModel(authorizedUserRepository), ScreenModel {
    private val tab by lazy { MutableStateFlow(UiModel.Tab.STATUSES) }
    private val statuses by lazy { MutableStateFlow<Map<UiModel.Tab, List<Status>>>(mapOf()) }

    val uiModel by lazy {
        buildUiModel(
            accountDetailRepository.attributes(id),
            tab,
            statuses,
            initialValue = UiModel.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        ) { account, tab, statuses ->
            UiModel.Loaded(
                account = account,
                tab = tab,
                statuses = statuses,
            )
        }
    }

    fun switch(tab: UiModel.Tab) {
        this.tab.value = tab
    }

    fun refreshStatuses() {
        val tabs = UiModel.Tab.entries.toTypedArray()

        /*
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
        */
    }

    fun loadStatusesMore() {
        /*
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
        */
    }

    sealed class UiModel {
        data object Loading : UiModel()
        data class Loaded(
            val account: AccountAttributes,
            val tab: Tab,
            val statuses: Map<Tab, List<Status>>,
        ) : UiModel() {
            val foreground = statuses[tab] ?: listOf()
        }

        enum class Tab {
            STATUSES, STATUSES_AND_REPLIES, MEDIA;

            fun buildQuery(maxId: StatusId? = null) = when (this) {
                STATUSES -> StatusesQuery.Default(maxId = maxId)
                STATUSES_AND_REPLIES -> StatusesQuery.WithReplies(maxId = maxId)
                MEDIA -> StatusesQuery.OnlyMedia(maxId = maxId)
            }
        }
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
