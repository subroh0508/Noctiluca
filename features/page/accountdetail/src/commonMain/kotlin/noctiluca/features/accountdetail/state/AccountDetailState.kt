package noctiluca.features.accountdetail.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.LocalScope
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.components.state.AuthorizedComposeState
import noctiluca.features.components.state.rememberAuthorizedComposeState
import noctiluca.features.components.state.runCatchingWithAuth
import noctiluca.model.AccountId
import noctiluca.status.model.Status
import org.koin.core.scope.Scope

internal data class AccountDetail(
    val attributes: AccountAttributes? = null,
    val status: List<Status> = listOf(),
)

internal class AccountDetailState(
    private val id: AccountId,
    private val detail: MutableState<AccountDetail>,
    private val state: AuthorizedComposeState,
    private val fetchAccountAttributesUseCase: FetchAccountAttributesUseCase,
) : State<AccountDetail> by detail, AuthorizedComposeState by state {
    constructor(
        id: AccountId,
        state: AuthorizedComposeState = AuthorizedComposeState(),
        koinScope: Scope = getKoinRootScope(),
    ) : this(
        id,
        mutableStateOf(AccountDetail()),
        state,
        koinScope.get(),
    )

    fun load(scope: CoroutineScope) {
        scope.launch {
            runCatchingWithAuth { fetchAccountAttributesUseCase.execute(id) }
                .onSuccess { detail.value = detail.value.copy(attributes = it) }
                .onFailure { /* back to previous page */ }
        }
    }
}

@Composable
internal fun rememberAccountDetail(
    id: AccountId,
    scope: Scope = LocalScope.current,
): AccountDetailState {
    val authorizationState = rememberAuthorizedComposeState()

    val detail = remember { AccountDetailState(id, authorizationState, scope) }

    LaunchedEffect(Unit) {
        detail.load(this)
    }

    return detail
}
