package noctiluca.features.accountdetail.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.features.accountdetail.LocalScope
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.components.state.AuthorizedComposeState
import noctiluca.features.components.state.rememberAuthorizedComposeState
import noctiluca.features.components.state.runCatchingWithAuth
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.status.model.Status
import org.koin.core.scope.Scope

data class AccountStatuses(
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

internal class AccountStatusesState(
    private val id: AccountId,
    private val statuses: MutableState<AccountStatuses>,
    private val state: AuthorizedComposeState,
    private val fetchAccountStatusesUseCase: FetchAccountStatusesUseCase,
) : State<AccountStatuses> by statuses, AuthorizedComposeState by state {
    constructor(
        id: AccountId,
        state: AuthorizedComposeState = AuthorizedComposeState(),
        koinScope: Scope = getKoinRootScope(),
    ) : this(
        id,
        mutableStateOf(AccountStatuses()),
        state,
        koinScope.get(),
    )

    fun switch(tab: AccountStatuses.Tab) {
        statuses.value = value.copy(tab = tab)
    }

    fun refresh(scope: CoroutineScope) {
        val tabs = AccountStatuses.Tab.values()

        tabs.forEach { t ->
            if (value.statuses[t]?.isNotEmpty() == true) {
                return@forEach
            }

            scope.launch {
                runCatchingWithAuth { fetchAccountStatusesUseCase.execute(id, t.buildQuery()) }
                    .onSuccess {
                        statuses.value = value.copy(statuses = value.statuses + mapOf(t to it))
                    }
                    .onFailure { }
            }
        }
    }

    fun loadMore(scope: CoroutineScope) {
        val tab = value.tab
        val foregroundStatuses = value.foreground

        scope.launch {
            runCatchingWithAuth {
                fetchAccountStatusesUseCase.execute(
                    id,
                    tab.buildQuery(foregroundStatuses.lastOrNull()?.id),
                )
            }
                .onSuccess {
                    statuses.value = value.copy(
                        statuses = value.statuses + mapOf(tab to foregroundStatuses + it),
                    )
                }
                .onFailure { }
        }
    }
}

@Composable
internal fun rememberAccountStatuses(
    id: AccountId,
    scope: Scope = LocalScope.current,
): AccountStatusesState {
    val authorizationState = rememberAuthorizedComposeState()

    val statuses = remember { AccountStatusesState(id, authorizationState, scope) }

    LaunchedEffect(Unit) {
        statuses.refresh(this)
    }

    return statuses
}
